package com.example.cafe.domain.form;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
public class CafeForm {
    private String title;
    private String introduce;
    private String genre;
    private String imgFileName;
}
