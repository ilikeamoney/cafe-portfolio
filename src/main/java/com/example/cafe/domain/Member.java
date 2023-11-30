package com.example.cafe.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * CREATE TABLE member(
 * 	   id INT PRIMARY KEY AUTO_INCREMENT,
 *     my_cafe_id INT,
 *     join_cafe_id VARCHAR(300),
 *     member_id VARCHAR(30) NOT NULL,
 *     member_pw VARCHAR(30) NOT NULL,
 *     name VARCHAR(15) NOT NULL,
 *     join_date DATETIME
 * );
 * **/

@Getter
@Builder
public class Member {
    private Integer id;
    private Integer myCafeId;
    private String joinCafeId;
    private String memberId;
    private String memberPw;
    private String name;
    private String joinDate;
}
