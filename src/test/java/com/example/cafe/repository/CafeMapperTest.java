package com.example.cafe.repository;

import com.example.cafe.domain.Cafe;
import com.example.cafe.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CafeMapperTest {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    CafeMapper cafeMapper;

    Integer memberId;

    Integer cafeId;

    @BeforeEach
    void setUp() {
        String memId = "qwer";
        String memPw = "1234";
        String name = "james";

        Integer count = memberMapper.duplicateIdCheck(memId);

        if (count == 0) {

            Member member = Member.builder()
                    .memberId(memId)
                    .memberPw(memPw)
                    .name(name)
                    .build();

            memberMapper.join(member);

            Integer login = memberMapper.login(member.getMemberId(), member.getMemberPw());
            if (login > 0) {
                memberId = login;
                Cafe cafe = Cafe.builder()
                        .adminId(memberId)
                        .title("cafe title")
                        .introduce("hello cafe")
                        .genre("커피")
                        .cafeImg("img.jpeg")
                        .build();

                cafeMapper.createCafe(cafe);
                cafeId = cafeMapper.getMyCafeId(memberId);
            }
        }
    }


    @Test
    @DisplayName("set my cafe test")
    void createCafe() throws Exception {
        // given
        memberMapper.setMyCafeId(memberId, cafeId);

        // when
        Member findMember = memberMapper.find(memberId);

        // then
        Assertions.assertThat(findMember.getMyCafeId()).isEqualTo(cafeId);
    }

    @Test
    @DisplayName("get set my crew test")
    void getSetCrew() throws Exception {
        // given
        Integer crewId = 30;
        String joinCrewId = String.valueOf(crewId);
        cafeMapper.setMyCrew(cafeId, joinCrewId);

        // when
        Integer id = 0;
        String myCrew = cafeMapper.getMyCrew(cafeId);

        if (!myCrew.contains(",")) {
            id = Integer.parseInt(myCrew);
        }

        // then
        Assertions.assertThat(id).isEqualTo(crewId);
    }

    @Test
    @DisplayName("find my cafe test")
    void findCafe() throws Exception {
        // given
        Cafe myCafe = cafeMapper.findMyCafe(memberId);

        // when
        String title = myCafe.getTitle();
        String introduce = myCafe.getIntroduce();


        // then
        Assertions.assertThat(title).isEqualTo("cafe title");
        Assertions.assertThat(introduce).isEqualTo("hello cafe");
        Assertions.assertThat(myCafe.getAdminId()).isEqualTo(memberId);
    }


}