package com.example.cafe.domain.form;

import lombok.Data;

@Data
public class BoardForm {
    private Integer memId;
    private Integer cafeId;
    private String title;
    private String content;
}
