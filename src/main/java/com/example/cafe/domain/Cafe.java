package com.example.cafe.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * CREATE TABLE cafe(
 *   id INT PRIMARY KEY AUTO_INCREMENT,
 *   title VARCHAR(30) NOT NULL,
 *   introduce VARCHAR(300) NOT NULL,
 *   cafe_img VARCHAR(255) NOT NULL,
 *   type VARCHAR (30) NOT NULL,
 *   crew_id VARCHAR(300)
 * );
 * **/

@Getter
@Builder
public class Cafe {
    private int id;
    private int adminId;
    private String title;
    private String introduce;
    private String cafeImg;
    private String genre;
    private String crewId;
    private String createDate;
}
