package com.example.cafe.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * CREATE TABLE comment(
 *    id INT PRIMARY KEY AUTO_INCREMENT,
 *    mem_id INT,
 *    title VARCHAR(50) NOT NULL,
 *    content VARCHAR(300) NOT NULL,
 *    write_date DATETIME,
 *    reg INT,
 *    re_level INT,
 *    re_step INT
 * );
 * **/

@Getter
@Builder
public class Comment {
    private Integer id;
    private Integer memId;
    private Integer cafeId;
    private String title;
    private String content;
    private String writeDate;
    private Integer reg;
    private Integer reLevel;
    private Integer reStep;
}
