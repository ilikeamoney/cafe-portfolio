package com.example.cafe.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *CREATE TABLE board(
 *  id INT PRIMARY KEY AUTO_INCREMENT,
 *  mem_id INT,
 *  title VARCHAR(50) NOT NULL,
 *  content VARCHAR(300) NOT NULL,
 *  write_date DATETIME,
 *  view_count INT
 *);
 * **/

@Getter
@Builder
public class Board {
    private Integer id;
    private Integer memId;
    private Integer cafeId;
    private String title;
    private String content;
    private String writeDate;
    private Integer viewCount;
}
