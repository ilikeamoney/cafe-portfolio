package com.example.cafe.service;

import com.example.cafe.domain.Board;
import com.example.cafe.domain.form.BoardForm;
import com.example.cafe.repository.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    public void writeBoard(BoardForm boardForm) {
        boardMapper.writeBoard(
                Board.builder()
                        .memId(boardForm.getMemId())
                        .cafeId(boardForm.getCafeId())
                        .title(boardForm.getTitle())
                        .content(boardForm.getContent())
                        .viewCount(0)
                        .build());
    }



}
