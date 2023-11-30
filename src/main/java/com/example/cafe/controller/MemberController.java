package com.example.cafe.controller;

import com.example.cafe.domain.Member;
import com.example.cafe.domain.form.MemberJoinForm;
import com.example.cafe.domain.form.MemberLoginForm;
import com.example.cafe.repository.MemberMapper;
import com.example.cafe.service.MemberService;
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

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    private final MemberMapper memberMapper;

    @PostMapping("/duplicationCheck")
    public String checkDuplication(@RequestParam("id") String id, Model model) {
        Integer check = memberMapper.duplicateIdCheck(id);
        model.addAttribute("check", check);
        return "views/duplication_check";
    }

    @PostMapping("/joinPro")
    public String joinPro(@ModelAttribute MemberJoinForm memberForm) {
        memberService.join(memberForm);
        return "redirect:/success";
    }

    @PostMapping("/loginPro")
    public String loginPro(@ModelAttribute MemberLoginForm memberLoginForm, Model model, HttpServletRequest req) {
        Integer memLog = memberService.login(memberLoginForm);
        HttpSession session = req.getSession();

        if (memLog != null) {
            session.setAttribute("memLog", memLog);
            Integer cafeId = memberService.isExistCafeId(memLog);

            log.info("cafeId = {}", cafeId);

            if (cafeId != null) {
                session.setAttribute("cafeId", cafeId);
            } else {
                session.setAttribute("cafeId", null);
            }
        }

        model.addAttribute("id", memLog);
        return "views/login_check";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.invalidate();
        return "redirect:/";
    }
}
