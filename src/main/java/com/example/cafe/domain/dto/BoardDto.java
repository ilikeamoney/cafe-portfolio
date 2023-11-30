package com.example.cafe.domain.dto;

import lombok.Data;

@Data
public class BoardDto {
    private Integer memId;
    private Integer boardId;
    private Integer cafeId;
    private String name;
    private String title;
    private String content;
    private Integer viewCount;
    private String writeDate;
}
