CREATE TABLE member(
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       my_cafe_id INT,
                       join_cafe_id VARCHAR(300),
                       member_id VARCHAR(30) NOT NULL,
                       member_pw VARCHAR(30) NOT NULL,
                       name VARCHAR(15) NOT NULL,
                       join_date DATETIME
);

CREATE TABLE board(
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      mem_id INT,
                      cafe_id INT,
                      title VARCHAR(50) NOT NULL,
                      content VARCHAR(300) NOT NULL,
                      write_date DATETIME,
                      view_count INT
);

CREATE TABLE comment(
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        mem_id INT,
                        cafe_id INT,
                        title VARCHAR(50) NOT NULL,
                        content VARCHAR(300) NOT NULL,
                        write_date DATETIME,
                        reg INT,
                        re_level INT,
                        re_step INT
);

CREATE TABLE cafe(
                     id INT PRIMARY KEY AUTO_INCREMENT,
                     admin_id INT,
                     title VARCHAR(30) NOT NULL,
                     introduce VARCHAR(300) NOT NULL,
                     cafe_img VARCHAR(255) NOT NULL,
                     genre VARCHAR (30) NOT NULL,
                     crew_id VARCHAR(300),
                     create_date DATETIME
);