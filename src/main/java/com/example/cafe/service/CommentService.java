package com.example.cafe.service;

import com.example.cafe.domain.Comment;
import com.example.cafe.domain.dto.CommentDto;
import com.example.cafe.domain.edit.CommentEdit;
import com.example.cafe.repository.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    public void writeComment(CommentDto commentDto) {
        commentMapper.insertCommentAlignment(commentDto.getReg(), commentDto.getReStep());
        commentMapper.writeComment(Comment.builder()
                .memId(commentDto.getMemId())
                .cafeId(commentDto.getCafeId())
                .title(commentDto.getTitle())
                .content(commentDto.getContent())
                .reg(commentDto.getReg())
                .reLevel(commentDto.getReLevel())
                .reStep(commentDto.getReStep())
                .build());
    }

    public void deleteComment(Integer commentId) {
        Comment comment = commentMapper.findComment(commentId);
        commentMapper.deleteCommentAlignment(comment.getReg(), comment.getReStep());
        commentMapper.deleteComment(commentId);
    }

    public void updateComment(Integer commentId, CommentEdit commentEdit) {
        commentMapper.updateComment(commentId, commentEdit);
    }
}
