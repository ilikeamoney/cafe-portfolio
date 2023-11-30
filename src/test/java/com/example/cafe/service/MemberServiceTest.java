package com.example.cafe.service;

import com.example.cafe.domain.Cafe;
import com.example.cafe.domain.Member;
import com.example.cafe.repository.CafeMapper;
import com.example.cafe.repository.MemberMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    CafeMapper cafeMapper;

    @Autowired
    CafeService cafeService;

    @Autowired
    MemberService memberService;

    List<Integer> cafeId = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 5; i++) {
            String memId = "qwer" + i;
            String memPw = "1234" + i;
            String name = "member" + i;

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
                    Member findMember = memberMapper.find(login);

                    Cafe cafe = Cafe.builder()
                            .adminId(findMember.getId())
                            .title("cafe" + i)
                            .introduce("introduce" + i)
                            .cafeImg("static" + i + ".jpeg")
                            .genre("blog")
                            .build();

                    cafeMapper.createCafe(cafe);
                    Cafe myCafe = cafeMapper.findMyCafe(login);
                    memberMapper.setMyCafeId(login, myCafe.getId());
                    cafeId.add(myCafe.getId());
                }
            }
        }
    }

    @Test
    @DisplayName("여러 카페를 가입하고 내가 가입한 카페 목록 보기")
    void seeJoinMyCafe() throws Exception {
        // given
        String memId = "zxcv";
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


            // when
            if (login > 0) {
                for (Integer id : cafeId) {
                    cafeService.joinCafe(id, login);
                }
            }

            List<Cafe> joinCafeList = memberService.getJoinCafeList(login);

            // then
            Assertions.assertThat(joinCafeList.get(1).getTitle()).isEqualTo("cafe1");
            Assertions.assertThat(joinCafeList.size()).isEqualTo(5);
        }
    }


    @Test
    @DisplayName("카페 탈퇴하기")
    void deleteCafe() throws Exception {
        // given
        String memId = "asdf";
        String memPw = "1234";
        String name = "kevin";

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
                for (Integer id : cafeId) {
                    cafeService.joinCafe(id, login);
                }
            }

            // when
            memberService.removeCafe(login, cafeId.get(2));

            List<Integer> joinCafeId = memberService.getJoinCafeId(login);
            List<Integer> crewId = cafeService.getCrewId(cafeId.get(2));

            // then
            Assertions.assertThat(joinCafeId.size()).isEqualTo(4);
            Assertions.assertThat(crewId).isNull();
        }
    }

    @Test
    @DisplayName("회원 한명이 여러 카페 가입하고 탈퇴")
    void oneMemberDeleteCafe() throws Exception {
        // given
        String memId = "asdf";
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
                for (Integer cafeId : cafeId) {
                    cafeService.joinCafe(cafeId, login);
                }

                // when
                memberService.removeCafe(login, cafeId.get(3));
                List<Integer> joinCafeId = memberService.getJoinCafeId(login);
                List<Integer> crewId1 = cafeService.getCrewId(cafeId.get(3));
                List<Integer> crewId2 = cafeService.getCrewId(cafeId.get(4));

                // then
                Assertions.assertThat(joinCafeId.size()).isEqualTo(4);
                Assertions.assertThat(crewId1).isNull();
                Assertions.assertThat(crewId2.get(0)).isEqualTo(login);
            }
        }
    }

    @Test
    @DisplayName("카페 두개중 한개만 가입하고 내가 가입한 카페인지 확인")
    void checkMyCafe() throws Exception {
        // given
        String memId = "asdf";
        String memPw = "1234";
        String name = "kevin";

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
                Integer cafeId1 = cafeId.get(0);
                Integer cafeId2 = cafeId.get(1);

                // when
                cafeService.joinCafe(cafeId1, login);

                boolean check1 = memberService.checkMyJoinCafe(login, cafeId1);
                boolean check2 = memberService.checkMyJoinCafe(login, cafeId2);

                // then
                Assertions.assertThat(check1).isTrue();
                Assertions.assertThat(check2).isFalse();
            }
        }
    }
}