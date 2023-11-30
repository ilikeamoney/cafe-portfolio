package com.example.cafe.domain.dto;

import lombok.Data;

@Data
public class CommentView {
    private Integer id;
    private Integer memId;
    private String name;
    private String title;
    private String content;
    private String writeDate;
}
