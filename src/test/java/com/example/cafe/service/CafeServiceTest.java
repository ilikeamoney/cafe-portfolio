package com.example.cafe.service;

import com.example.cafe.domain.Board;
import com.example.cafe.domain.Cafe;
import com.example.cafe.domain.Member;
import com.example.cafe.repository.BoardMapper;
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
class CafeServiceTest {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    CafeMapper cafeMapper;

    @Autowired
    CafeService cafeService;

    @Autowired
    MemberService memberService;

    @Autowired
    BoardMapper boardMapper;

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
                Member findMember = memberMapper.find(login);

                Cafe cafe = Cafe.builder()
                        .adminId(findMember.getId())
                        .title("cafe title")
                        .introduce("cafe introduce")
                        .cafeImg("img.jpeg")
                        .genre("animal")
                        .build();

                cafeMapper.createCafe(cafe);
                cafeId = cafeMapper.getMyCafeId(findMember.getId());
                memberMapper.setMyCafeId(findMember.getId(), cafeId);
            }
        }
    }

    @Test
    @DisplayName("하나의 카페에 한명 가입")
    void joinOneMemberCafe() throws Exception {
        // given
        String memId = "wow";
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
                // when
                cafeService.joinCafe(cafeId, login);

                Member findMember = memberMapper.find(login);
                Integer crewId = cafeService.getCrewId(cafeId).get(0);
                Integer joinCafeId = memberService.getJoinCafeId(findMember.getId()).get(0);

                // then
                Assertions.assertThat(crewId).isEqualTo(login);
                Assertions.assertThat(joinCafeId).isEqualTo(cafeId);
            }
        }
    }

    @Test
    @DisplayName("한개의 카페에 여러명 가입")
    void membersJoinCafe() throws Exception {
        // given

        // 회원 5명 생성
        for (int i = 0; i < 5; i++) {
            Member member = Member.builder()
                    .memberId("qwer" + i)
                    .memberPw("1234")
                    .name("james" + i)
                    .build();

            memberMapper.join(member);

            // 로그인 뒤 카페 가입
            Integer login = memberMapper.login(member.getMemberId(), member.getMemberPw());
            cafeService.joinCafe(cafeId, login);
        }

        // when
        // 가입회원 목록 가져오기
        List<Integer> crewId = cafeService.getCrewId(cafeId);

        // then
        Assertions.assertThat(crewId.size()).isEqualTo(5);
        Assertions.assertThat(crewId).contains(2, 3, 4, 5, 6);
        Assertions.assertThat(Integer.parseInt(memberMapper.find(3).getJoinCafeId())).isEqualTo(cafeId);
    }

    @Test
    @DisplayName("카페에 가입한 회원 목록 가져오기")
    void joinCafeMemberList() throws Exception {
        // given
        for (int i = 0; i < 5; i++) {
            Member member = Member.builder()
                    .memberId("qwer" + i)
                    .memberPw("1234")
                    .name("james" + i)
                    .build();

            memberMapper.join(member);

            // 로그인 뒤 카페 가입
            Integer login = memberMapper.login(member.getMemberId(), member.getMemberPw());
            cafeService.joinCafe(cafeId, login);
        }

        // when
        List<Member> cafeMembers = cafeService.getCafeMembers(cafeId);

        // then
        Assertions.assertThat(cafeMembers.size()).isEqualTo(5);
        Assertions.assertThat(cafeMembers.get(0).getMemberId()).isEqualTo("qwer0");
    }

    @Test
    @DisplayName("카페에 작성된 게시글 가져오기")
    void getMyCafeBoard() throws Exception {
        // given
        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .memberId("qwer" + i)
                    .memberPw("1234")
                    .name("james" + i)
                    .build();

            memberMapper.join(member);
            Integer login = memberMapper.login(member.getMemberId(), member.getMemberPw());
            cafeService.joinCafe(cafeId, login);

            for (int j = 0; j < 10; j++) {
                Board board = Board.builder()
                        .memId(login)
                        .cafeId(cafeId)
                        .title("title" + i)
                        .content("contetn" + i)
                        .viewCount(0)
                        .build();

                // 게시글 10개 씩 총 10번 = 100개
                boardMapper.writeBoard(board);
            }
        }

        // when
        List<Board> cafeBoards = cafeService.getCafeBoards(cafeId, 0, 5);

        // then
        Assertions.assertThat(cafeBoards.size()).isEqualTo(5);
        Assertions.assertThat(cafeBoards.get(0).getId()).isEqualTo(100);
    }

    @Test
    @DisplayName("특정 회원의 게시글만 가져오기")
    void getMemberBoard() throws Exception {
        // given
        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .memberId("qwer" + i)
                    .memberPw("1234")
                    .name("james" + i)
                    .build();

            memberMapper.join(member);
            Integer login = memberMapper.login(member.getMemberId(), member.getMemberPw());
            cafeService.joinCafe(cafeId, login);

            for (int j = 0; j < 10; j++) {
                Board board = Board.builder()
                        .memId(login)
                        .cafeId(cafeId)
                        .title("title" + i)
                        .content("contetn" + i)
                        .viewCount(0)
                        .build();

                // 게시글 10개 씩 총 10번 = 100개
                boardMapper.writeBoard(board);
            }
        }

        // when
        List<Board> memberBoards = cafeService.getMemberBoards(cafeId, 3, 0, 5);

        // then
        Assertions.assertThat(memberBoards.size()).isEqualTo(5);
        Assertions.assertThat(memberBoards.get(0).getMemId()).isEqualTo(3);
        Assertions.assertThat(memberBoards.get(0).getMemId()).isEqualTo(3);
    }

    @Test
    @DisplayName("카페 회원 탈퇴시키면 가입 회원 아이디 삭제")
    void removeMemberId() throws Exception {
        // given
        List<Integer> ids = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Member member = Member.builder()
                    .memberId("qwer" + i)
                    .memberPw("1234")
                    .name("james" + i)
                    .build();

            memberMapper.join(member);
            Integer login = memberMapper.login(member.getMemberId(), member.getMemberPw());

            if (login > 0) {
                cafeService.joinCafe(cafeId, login);
                ids.add(login);
            }
        }

        // when
        cafeService.removeMember(cafeId, ids.get(3));
        List<Integer> crewId = cafeService.getCrewId(cafeId);

        // then
        Assertions.assertThat(crewId.size()).isEqualTo(4);
        Assertions.assertThat(memberService.getJoinCafeId(ids.get(3))).isNull();
    }

    @Test
    @DisplayName("가입한 회원의 아이디 체크")
    void checkJoinCafe() throws Exception {
        // given
        List<Member> members = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            String memId = "qwer" + i;
            String memPw = "1234" + i;
            String name = "james" + i;

            Integer count = memberMapper.duplicateIdCheck(memId);

            if (count == 0) {
                Member member = Member.builder()
                        .memberId(memId)
                        .memberPw(memPw)
                        .name(name)
                        .build();

                memberMapper.join(member);
                members.add(member);
            }
        }

        Member mem1 = members.get(0);
        Member mem2 = members.get(1);

        Integer memLog1 = memberMapper.login(mem1.getMemberId(), mem1.getMemberPw());
        Integer memLog2 = memberMapper.login(mem2.getMemberId(), mem2.getMemberPw());

        // when
        cafeService.joinCafe(cafeId, memLog1);
        boolean check1 = cafeService.checkMyCrew(cafeId, memLog1);
        boolean check2 = cafeService.checkMyCrew(cafeId, memLog2);

        // then
        Assertions.assertThat(check1).isTrue();
        Assertions.assertThat(check2).isFalse();
    }
}