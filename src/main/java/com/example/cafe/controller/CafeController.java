package com.example.cafe.controller;

import com.example.cafe.domain.Board;
import com.example.cafe.domain.Cafe;
import com.example.cafe.domain.Member;
import com.example.cafe.repository.CafeMapper;
import com.example.cafe.repository.MemberMapper;
import com.example.cafe.service.CafeService;
import com.example.cafe.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.Mode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CafeController {

    private final CafeMapper cafeMapper;

    private final CafeService cafeService;

    private final MemberService memberService;

    private final MemberMapper memberMapper;

    // 이미지가 저장될 경로 application.properties 에 설정
    @Value("${file.dir}")
    private String fileDir;
    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    @GetMapping("/create-cafe-form")
    public String createCafeForm() {
        return "views/create_cafe_form";
    }

    @GetMapping("/cafe-join")
    public String joinCafe(@RequestParam Integer cafeId, HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
        Integer memLog = (Integer) session.getAttribute("memLog");
        cafeService.joinCafe(cafeId, memLog);
        model.addAttribute("cafeId", cafeId);
        return "views/cafe_join_pro";
    }

    @GetMapping("/remove-join-cafe")
    public String removeCafe(HttpServletRequest req, @RequestParam Integer cafeId, Model model) {
        HttpSession session = req.getSession();
        Integer memLog = (Integer) session.getAttribute("memLog");
        memberService.removeCafe(memLog, cafeId);
        model.addAttribute("cafeId", cafeId);
        return "views/cafe_remove_pro";
    }

    @GetMapping("/remove-join-member")
    public String removeJoinMember(@RequestParam("cafeId") Integer cafeId,
                                   @RequestParam("memId") Integer memId,
                                   Model model) {
        cafeService.removeMember(cafeId, memId);
        model.addAttribute("cafeId", cafeId);
        return "views/remove_member_init";
    }

    // 카페 생성 데이터
    @PostMapping("/send-my-cafe-data")
    public String createCafeFormPost(@RequestParam("title") String title,
                                     @RequestParam("introduce") String introduce,
                                     @RequestParam("genre") String genre,
                                     @RequestParam("imgFile") MultipartFile imgFile,
                                     HttpServletRequest req) throws IOException {

        HttpSession session = req.getSession();
        Integer memLog = (Integer) session.getAttribute("memLog");
        String fileName = cafeService.createNotDuplicateFileName(imgFile.getOriginalFilename());

        imgFile.transferTo(new File(getFullPath(fileName)));
        cafeService.createCafe(memLog, title, introduce, genre, fileName);

        Integer cafeId = memberService.isExistCafeId(memLog);

        if (cafeId != null) {
            session.setAttribute("cafeId", cafeId);
        }
        return "redirect:/";
    }

    // 나의 카페 게시글 목록
    @GetMapping("/show-my-cafe")
    public String showMyCafe(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();

        String page = req.getParameter("page");
        boolean minusBtn = Boolean.parseBoolean(req.getParameter("minusBtn"));
        boolean plusBtn = Boolean.parseBoolean(req.getParameter("plusBtn"));
        int currentPage = (Integer) session.getAttribute("currentPage");
        int btnStart = (Integer) session.getAttribute("btnStart");
        int btnEnd = (Integer) session.getAttribute("btnEnd");
        int cafeId = (Integer) session.getAttribute("cafeId");
        int cafeTotalBoardCount = cafeMapper.getCafeBoardCount(cafeId);
        int viewStart = 1;
        int viewCount = 10;
        int pageCount = cafeTotalBoardCount / viewCount;
        Cafe cafe = cafeMapper.find(cafeId);

        if (cafeTotalBoardCount % viewCount != 0 || cafeTotalBoardCount == 0) {
            pageCount += 1;
        }

        if (page != null) {
            viewStart = Integer.parseInt(page);
            currentPage = viewStart;

            if (btnStart + 1 == currentPage && currentPage > 1) {
                btnStart -= 1;
                btnEnd -= 1;
            } else if (btnEnd == currentPage && currentPage < cafeTotalBoardCount) {
                btnStart += 1;
                btnEnd += 1;
            }

            session.setAttribute("currentPage", currentPage);
            session.setAttribute("btnStart", btnStart);
            session.setAttribute("btnEnd", btnEnd);
        }

        if (minusBtn) {
            currentPage -= 1;
            if (currentPage < 1) {
                currentPage = 1;
            }

            if (btnStart + 1 == currentPage && currentPage > 1) {
                btnStart -= 1;
                btnEnd -= 1;
            }

            viewStart = currentPage;
            session.setAttribute("currentPage", viewStart);
            session.setAttribute("btnStart", btnStart);
            session.setAttribute("btnEnd", btnEnd);

            return "views/my_cafe_btn_init";
        } else if (plusBtn) {
            currentPage += 1;
            if (currentPage > pageCount) {
                currentPage = pageCount;
            }

            if (btnEnd == currentPage && currentPage < pageCount) {
                btnStart += 1;
                btnEnd += 1;
            }

            viewStart = currentPage;
            session.setAttribute("currentPage", viewStart);
            session.setAttribute("btnStart", btnStart);
            session.setAttribute("btnEnd", btnEnd);

            return "views/my_cafe_btn_init";
        }

        if (btnEnd > pageCount) {
            btnEnd = pageCount;
        }

        viewStart = (viewStart - 1) * viewCount;
        List<Board> cafeBoards = cafeService.getCafeBoards(cafeId, viewStart, viewCount);

        model.addAttribute("myCafe", cafe);
        model.addAttribute("btnStart", btnStart);
        model.addAttribute("btnEnd", btnEnd);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("cafeBoards", cafeBoards);
        model.addAttribute("totalBoardCount", cafeTotalBoardCount);
        return "views/show_my_cafe";
    }

    // 가입한 카페목록을 보여주는 메소드
    @GetMapping("/show-join-cafe")
    public String showJoinCafe(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
        Integer memLog = (Integer) session.getAttribute("memLog");

        Member member = memberMapper.find(memLog);
        List<Cafe> joinCafeList = memberService.getJoinCafeList(memLog);

        if (joinCafeList != null) {
            model.addAttribute("cafeList", joinCafeList);
            model.addAttribute("cafeSize", joinCafeList.size());
        }

        model.addAttribute("member", member);
        return "views/show-join-cafe";
    }

    // 가입한 카페에 들어가면 게시글을 보여주는 메소드
    @GetMapping("/cafe-detail")
    public String cafeDetail(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();

        String page = req.getParameter("page");
        boolean minusBtn = Boolean.parseBoolean(req.getParameter("minusBtn"));
        boolean plusBtn = Boolean.parseBoolean(req.getParameter("plusBtn"));
        int currentPage = (Integer) session.getAttribute("currentPage");
        int btnStart = (Integer) session.getAttribute("btnStart");
        int btnEnd = (Integer) session.getAttribute("btnEnd");
        int memLog = (Integer) session.getAttribute("memLog");
        int cafeId = Integer.parseInt(req.getParameter("cafeId"));
        int cafeTotalBoardCount = cafeMapper.getCafeBoardCount(cafeId);
        int viewStart = 1;
        int viewCount = 10;
        int pageCount = cafeTotalBoardCount / viewCount;
        boolean crewCheck = cafeService.checkMyCrew(cafeId, memLog);
        boolean adminCheck = cafeService.checkIsAdmin(cafeId, memLog);
        Cafe cafe = cafeMapper.find(cafeId);

        if (cafeTotalBoardCount % viewCount != 0 || cafeTotalBoardCount == 0) {
            pageCount += 1;
        }

        if (page != null) {
            viewStart = Integer.parseInt(page);
            currentPage = viewStart;

            if (btnStart + 1 == currentPage && currentPage > 1) {
                btnStart -= 1;
                btnEnd -= 1;
            } else if (btnEnd == currentPage && currentPage < cafeTotalBoardCount) {
                btnStart += 1;
                btnEnd += 1;
            }

            session.setAttribute("currentPage", currentPage);
            session.setAttribute("btnStart", btnStart);
            session.setAttribute("btnEnd", btnEnd);
        }

        if (minusBtn) {
            currentPage -= 1;
            if (currentPage < 1) {
                currentPage = 1;
            }

            if (btnStart + 1 == currentPage && currentPage > 1) {
                btnStart -= 1;
                btnEnd -= 1;
            }

            viewStart = currentPage;
            session.setAttribute("currentPage", viewStart);
            session.setAttribute("btnStart", btnStart);
            session.setAttribute("btnEnd", btnEnd);
            model.addAttribute("cafeId", cafeId);

            return "views/find_cafe_btn_init";
        } else if (plusBtn) {
            currentPage += 1;
            if (currentPage > pageCount) {
                currentPage = pageCount;
            }

            if (btnEnd == currentPage && currentPage < pageCount) {
                btnStart += 1;
                btnEnd += 1;
            }

            viewStart = currentPage;
            session.setAttribute("currentPage", viewStart);
            session.setAttribute("btnStart", btnStart);
            session.setAttribute("btnEnd", btnEnd);
            model.addAttribute("cafeId", cafeId);

            return "views/find_cafe_btn_init";
        }

        if (btnEnd > pageCount) {
            btnEnd = pageCount;
        }

        viewStart = (viewStart - 1) * viewCount;
        List<Board> cafeBoards = cafeService.getCafeBoards(cafeId, viewStart, viewCount);

        model.addAttribute("crewCheck", crewCheck);
        model.addAttribute("adminCheck", adminCheck);
        model.addAttribute("cafe", cafe);
        model.addAttribute("btnStart", btnStart);
        model.addAttribute("btnEnd", btnEnd);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("cafeBoards", cafeBoards);
        model.addAttribute("totalBoardCount", cafeTotalBoardCount);
        return "views/cafe_detail";
    }

    @GetMapping("/show-all-cafe")
    public String showALlCafe(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();

        String page = req.getParameter("page");
        boolean minusBtn = Boolean.parseBoolean(req.getParameter("minusBtn"));
        boolean plusBtn = Boolean.parseBoolean(req.getParameter("plusBtn"));
        int currentPage = (Integer) session.getAttribute("currentPage");
        int btnStart = (Integer) session.getAttribute("btnStart");
        int btnEnd = (Integer) session.getAttribute("btnEnd");
        int viewStart = 1;
        int viewCount = 10;
        int totalCafeCount = cafeMapper.getTotalCafeCount();
        int pageCount = totalCafeCount / viewCount;

        if (totalCafeCount % viewCount != 0 || totalCafeCount == 0) {
            pageCount += 1;
        }

        if (page != null) {
            viewStart = Integer.parseInt(page);
            currentPage = viewStart;

            if (btnStart - 1 == currentPage && currentPage > 1) {
                btnStart -= 1;
                btnEnd -= 1;
            } else if (btnEnd == currentPage && currentPage < pageCount) {
                btnStart += 1;
                btnEnd += 1;
            }

            session.setAttribute("currentPage", currentPage);
            session.setAttribute("btnStart", btnStart);
            session.setAttribute("btnEnd", btnEnd);
        }

        if (minusBtn) {
            currentPage -= 1;
            if (currentPage < 1) {
                currentPage = 1;
            }

            if (btnStart + 1 == currentPage && currentPage > 1) {
                btnStart -= 1;
                btnEnd -= 1;
            }

            viewStart = currentPage;
            session.setAttribute("currentPage", viewStart);
            session.setAttribute("btnStart", btnStart);
            session.setAttribute("btnEnd", btnEnd);

            return "views/all_cafe_btn_init";
        } else if (plusBtn) {
            currentPage += 1;
            if (currentPage > pageCount) {
                currentPage = pageCount;
            }

            if (btnEnd == currentPage && currentPage < pageCount) {
                btnStart += 1;
                btnEnd += 1;
            }

            viewStart = currentPage;
            session.setAttribute("currentPage", viewStart);
            session.setAttribute("btnStart", btnStart);
            session.setAttribute("btnEnd", btnEnd);

            return "views/all_cafe_btn_init";
        }

        if (btnEnd > pageCount) {
            btnEnd = pageCount;
        }

        viewStart = (viewStart - 1) * viewCount;
        List<Cafe> cafeList = cafeMapper.getAllCafe(viewStart, viewCount);

        model.addAttribute("btnStart", btnStart);
        model.addAttribute("btnEnd", btnEnd);
        model.addAttribute("cafeList", cafeList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalCafeCount", totalCafeCount);
        return "views/show-all-cafe";
    }

    // 내 회원들 보여주는 메소드
    @GetMapping("/show-my-member")
    public String showMyMember(@RequestParam("cafeId") Integer cafeId,
                               Model model) {

        Cafe findCafe = cafeMapper.find(cafeId);
        Member admin = memberMapper.find(cafeMapper.getMyAdminId(cafeId));
        List<Member> cafeMembers = cafeService.getCafeMembers(cafeId);

        model.addAttribute("admin", admin);
        model.addAttribute("members", cafeMembers);
        model.addAttribute("cafe", findCafe);
        return "views/show_my_members";
    }

    @GetMapping("/delete-cafe")
    public String deleteCafe(HttpServletRequest req, @RequestParam("cafeId") Integer cafeId, Model model) {
        HttpSession session = req.getSession();
        Integer memLog = (Integer) session.getAttribute("memLog");
        Cafe cafe = cafeMapper.find(cafeId);
        String fullPath = getFullPath(cafe.getCafeImg());
        cafeService.deleteCafeImg(fullPath);
        cafeService.deleteCafe(cafeId, memLog);

        Member member = memberMapper.find(memLog);
        model.addAttribute("memId", member.getMemberId());
        model.addAttribute("memPw", member.getMemberPw());
        return "views/delete_cafe_pro";
    }

    // 이미지 로드를 하기 위해서 인프런 보고 사용한 코드
    @ResponseBody
    @GetMapping("/image/{fileName}")
    public Resource  downLoadImg(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + getFullPath(fileName));
    }


}
