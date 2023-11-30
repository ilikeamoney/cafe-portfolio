package com.example.cafe.domain.edit;

import lombok.Data;

@Data
public class BoardEdit {
    private Integer boardId;
    private Integer cafeId;
    private String title;
    private String content;
}
