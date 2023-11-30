package com.example.cafe.controller;

import com.example.cafe.domain.Board;
import com.example.cafe.domain.dto.BoardDto;
import com.example.cafe.domain.dto.CommentView;
import com.example.cafe.domain.edit.BoardEdit;
import com.example.cafe.domain.form.BoardForm;
import com.example.cafe.repository.BoardMapper;
import com.example.cafe.repository.CommentMapper;
import com.example.cafe.service.BoardService;
import com.example.cafe.service.CafeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardMapper boardMapper;
    private final BoardService boardService;
    private final CafeService cafeService;
    private final CommentMapper commentMapper;

    @GetMapping("/join-cafe-write-board")
    public String writeBoard(HttpServletRequest req, Model model) {
        int cafeId = Integer.parseInt(req.getParameter("cafeId"));
        model.addAttribute("cafeId", cafeId);
        return "views/join_cafe_write_board_form";
    }

    @PostMapping("/write-board-pro")
    public String writeBoardPro(@ModelAttribute BoardForm boardForm, HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
        Integer currentPage = (Integer) session.getAttribute("currentPage");
        boardService.writeBoard(boardForm);

        model.addAttribute("cafeId", boardForm.getCafeId());
        model.addAttribute("currentPage", currentPage);
        return "views/write_board_pro";
    }

    @GetMapping("/board-detail")
    public String boardDetail(HttpServletRequest req,
                              @RequestParam("boardId") Integer boardId,
                              Model model) {
        // 조회수 올리고 지금 게시글에 들어온 회원이 카페 회원인지 체크
        HttpSession session = req.getSession();
        boardMapper.increaseViewCount(boardId);
        BoardDto board = boardMapper.findBoardDetail(boardId);
        Integer memLog = (Integer) session.getAttribute("memLog");
        boolean check = cafeService.checkMyCrew(board.getCafeId(), memLog);

        // 페이징
        String page = req.getParameter("page");
        boolean minusBtn = Boolean.parseBoolean(req.getParameter("minusBtn"));
        boolean plusBtn = Boolean.parseBoolean(req.getParameter("plusBtn"));
        int currentPage = (Integer) session.getAttribute("currentPage");
        int btnStart = (Integer) session.getAttribute("btnStart");
        int btnEnd = (Integer) session.getAttribute("btnEnd");
        int totalBoardCount = commentMapper.countBoardComment(boardId);
        int viewStart = 1;
        int viewCount = 10;
        int pageCount = totalBoardCount / viewCount;

        if (totalBoardCount % viewCount != 0 || totalBoardCount == 0) {
            pageCount += 1;
        }

        if (page != null) {
            viewStart = Integer.parseInt(page);
            currentPage = viewStart;

            if (btnStart + 1 == currentPage && currentPage > 1) {
                btnStart -= 1;
                btnEnd -= 1;
            } else if (btnEnd == currentPage && currentPage < totalBoardCount) {
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

            model.addAttribute("boardId", boardId);
            return "views/comment_btn_init";
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

            model.addAttribute("boardId", boardId);
            return "views/comment_btn_init";
        }

        if (btnEnd > pageCount) {
            btnEnd = pageCount;
        }

        viewStart = (viewStart - 1) * viewCount;
        List<CommentView> comments = commentMapper.getBoardComments(boardId, viewStart, viewCount);

        model.addAttribute("btnStart", btnStart);
        model.addAttribute("btnEnd", btnEnd);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("comments", comments);
        model.addAttribute("totalBoardCount", totalBoardCount);
        model.addAttribute("memberCheck", check);
        model.addAttribute("board", board);
        return "views/board_detail";
    }

    @GetMapping("/board-update")
    public String boardUpdateAct(@RequestParam("boardId") Integer boardId, Model model) {
        Board board = boardMapper.findBoard(boardId);

        model.addAttribute("board", board);
        return "views/board_update";
    }

    @PostMapping("/board-update-pro")
    public String boardUpdatePro(@ModelAttribute BoardEdit boardEdit, Model model) {
        boardMapper.updateBoard(boardEdit);
        model.addAttribute("boardId", boardEdit.getBoardId());
        return "views/board_update_pro";
    }

    @GetMapping("/board-delete")
    public String boardDelete(@RequestParam("boardId") Integer boardId,
                              @RequestParam("cafeId") Integer cafeId,
                              Model model) {
        boardMapper.deleteBoard(boardId);
        commentMapper.deleteBoardComment(boardId);

        model.addAttribute("cafeId", cafeId);
        return "views/delete_board";
    }
}
