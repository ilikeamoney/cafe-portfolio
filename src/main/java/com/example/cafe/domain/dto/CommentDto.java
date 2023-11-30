package com.example.cafe.domain.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Integer memId;
    private Integer cafeId;
    private String title;
    private String content;
    private Integer reg;
    private Integer reLevel;
    private Integer reStep;
}
