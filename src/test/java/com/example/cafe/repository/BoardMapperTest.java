package com.example.cafe.repository;

import com.example.cafe.domain.Board;
import com.example.cafe.domain.Cafe;
import com.example.cafe.domain.Member;
import com.example.cafe.domain.edit.BoardEdit;
import com.example.cafe.service.CafeService;
import com.example.cafe.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BoardMapperTest {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    BoardMapper boardMapper;

    @Autowired
    CafeMapper cafeMapper;

    @Autowired
    CafeService cafeService;

    @Autowired
    MemberService memberService;

    @Value("${file.dir}")
    String value;

    Member member;
    Integer memberId;
    Integer cafeId;

    @BeforeEach
    void setUp() {
        Integer login = 0;
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

            login = memberMapper.login(member.getMemberId(), member.getMemberPw());
            if (login > 0) {
                Cafe cafe = Cafe.builder()
                        .adminId(login)
                        .title("title")
                        .introduce("introduce")
                        .cafeImg("img.jpeg")
                        .genre("sport")
                        .build();

                cafeMapper.createCafe(cafe);
                Integer myCafeId = cafeMapper.getMyCafeId(login);
                memberMapper.setMyCafeId(login, myCafeId);
                cafeId = myCafeId;
            }
        }

        memId = "asdf";
        memPw = "1234";
        name = "kevin";

        count = memberMapper.duplicateIdCheck(memId);
        if (count == 0) {
            member = Member.builder()
                    .memberId(memId)
                    .memberPw(memPw)
                    .name(name)
                    .build();

            memberMapper.join(member);

            login = memberMapper.login(member.getMemberId(), member.getMemberPw());
            if (login > 0) {
                memberId = login;
            }
        }
    }

    @Test
    @DisplayName("카페에 게시글 작성")
    void writeBoardTest() throws Exception {
        //given
        for (int i = 0; i < 5; i++) {
            Board board = Board.builder()
                    .memId(memberId)
                    .cafeId(cafeId)
                    .title("title")
                    .content("content")
                    .viewCount(0)
                    .build();

            boardMapper.writeBoard(board);
        }

        // when
        Integer count = boardMapper.countCafeBoard(cafeId);

        // then
        Assertions.assertThat(count).isEqualTo(5);
    }

    @Test
    @DisplayName("게시글 찾기")
    void findBoard() throws Exception {
        // given
        Board board = Board.builder()
                .memId(memberId)
                .cafeId(cafeId)
                .title("title")
                .content("content")
                .viewCount(0)
                .build();

        boardMapper.writeBoard(board);
        // when
        Integer id = 1;
        Board findBoard = boardMapper.findBoard(id);

        // then
        Assertions.assertThat(findBoard.getTitle()).isEqualTo("title");
        Assertions.assertThat(findBoard.getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("게시글 수정")
    void editBoard() throws Exception {
        // given
        Board board = Board.builder()
                .memId(memberId)
                .cafeId(cafeId)
                .title("title")
                .content("content")
                .viewCount(0)
                .build();

        boardMapper.writeBoard(board);

        // when
        Integer id = 1;
        BoardEdit boardEdit = new BoardEdit();
        boardEdit.setTitle("wow");
        boardEdit.setContent("hello");
        boardMapper.updateBoard(id, boardEdit);
        Board findBoard = boardMapper.findBoard(id);

        // then
        Assertions.assertThat(findBoard.getTitle()).isEqualTo("wow");
        Assertions.assertThat(findBoard.getContent()).isEqualTo("hello");
    }

    @Test
    @DisplayName("나의 게시글 전체 삭제")
    void deleteAll() throws Exception {
        // given
        for (int i = 0; i < 10; i++) {
            Board board = Board.builder()
                    .memId(memberId)
                    .cafeId(cafeId)
                    .title("title")
                    .content("content")
                    .viewCount(0)
                    .build();
            boardMapper.writeBoard(board);
        }
        // when
        boardMapper.deleteAllMylBoard(memberId);
        Integer count = boardMapper.countMyBoard(memberId);

        // then
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 삭제")
    void deleteBoard() throws Exception {
        // given
        Board board = Board.builder()
                .memId(memberId)
                .cafeId(cafeId)
                .title("title")
                .content("content")
                .viewCount(0)
                .build();

        boardMapper.writeBoard(board);

        // when
        Integer id = 1;
        boardMapper.deleteBoard(id);
        Integer count = boardMapper.countAllBoard();

        // then
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("회원이 카페에 탈퇴하면 가입한 카페에 게시글이 삭제되는지 확인")
    void checkCafeBoardCount() throws Exception {
        // given
        cafeService.joinCafe(cafeId, memberId);

        for (int i = 0; i < 10; i++) {
            Board board = Board.builder()
                    .memId(memberId)
                    .cafeId(cafeId)
                    .title("title" + i)
                    .content("content" + i)
                    .viewCount(0)
                    .build();

            boardMapper.writeBoard(board);
        }

        // when
        memberService.removeCafe(memberId, cafeId);
        Integer count = boardMapper.countCafeBoard(cafeId);
        List<Integer> joinCafeId = memberService.getJoinCafeId(memberId);
        List<Integer> crewId = cafeService.getCrewId(cafeId);

        // then
        Assertions.assertThat(count).isEqualTo(0);
        Assertions.assertThat(crewId).isNull();
        Assertions.assertThat(joinCafeId).isNull();
    }


    @Test
    @DisplayName("카페에서 회원을 탈퇴 시키면 작성한 게시글이 삭제되는지 확인")
    void checkRemoveMemberBoard() throws Exception {
        // given
        cafeService.joinCafe(cafeId, memberId);

        for (int i = 0; i < 10; i++) {
            Board board = Board.builder()
                    .memId(memberId)
                    .cafeId(cafeId)
                    .title("title" + i)
                    .content("content" + i)
                    .viewCount(0)
                    .build();

            boardMapper.writeBoard(board);
        }

        // when
        cafeService.removeMember(cafeId, memberId);
        Integer count = boardMapper.countMyBoard(memberId);
        List<Integer> joinCafeId = memberService.getJoinCafeId(memberId);
        List<Integer> crewId = cafeService.getCrewId(cafeId);

        // then
        Assertions.assertThat(count).isEqualTo(0);
        Assertions.assertThat(joinCafeId).isNull();
        Assertions.assertThat(crewId).isNull();
    }
}