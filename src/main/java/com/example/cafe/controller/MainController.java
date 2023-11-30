package com.example.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {
    @GetMapping("/main")
    public String mainPage(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("currentPage", 1);
        session.setAttribute("btnStart", 0);
        session.setAttribute("btnEnd", 5);
        return "views/main";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "views/login_form";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "views/join_form";
    }

    @GetMapping("/success")
    public String success() {
        return "views/join_success";
    }

}
