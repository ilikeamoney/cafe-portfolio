package com.example.cafe.controller;

import com.example.cafe.domain.dto.CommentDto;
import com.example.cafe.repository.CommentMapper;
import com.example.cafe.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @PostMapping("/write-comment")
    public String writeComment(@ModelAttribute CommentDto commentDto, Model model) {
        commentService.writeComment(commentDto);
        model.addAttribute("reg", commentDto.getReg());
        return "views/write_comment";
    }

    @GetMapping("/delete-comment")
    public String deleteComment(@RequestParam("commentId") Integer commentId,
                                @RequestParam("boardId") Integer boardId,
                                Model model) {
        commentService.deleteComment(commentId);
        model.addAttribute("boardId", boardId);
        return "views/delete_comment_init";
    }
}
