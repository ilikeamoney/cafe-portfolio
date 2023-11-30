package com.example.cafe.repository;

import com.example.cafe.domain.Board;
import com.example.cafe.domain.Cafe;
import com.example.cafe.domain.Comment;
import com.example.cafe.domain.Member;
import com.example.cafe.domain.edit.CommentEdit;
import com.example.cafe.service.CafeService;
import com.example.cafe.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommentMapperTest {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    BoardMapper boardMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    CafeMapper cafeMapper;

    @Autowired
    CafeService cafeService;

    @Autowired
    MemberService memberService;

    Integer cafeId;
    Integer memberId;

    @BeforeEach
    void setUp() {
        // 회원 가입후 카페 창설
        Integer cafeId = 0;
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
                Cafe cafe = Cafe.builder()
                        .adminId(login)
                        .title("title")
                        .introduce("introduce")
                        .cafeImg("img.jpeg")
                        .genre("cafe")
                        .build();

                cafeMapper.createCafe(cafe);
                Integer myCafeId = cafeMapper.getMyCafeId(login);
                memberMapper.setMyCafeId(login, myCafeId);
                cafeId = myCafeId;
            }
        }

        // 카페에 가입해서 게시글 작성
        memId = "asdf";
        memPw = "1234";
        name = "kevin";

        count = memberMapper.duplicateIdCheck(memId);
        if (count == 0) {
            Member member = Member.builder()
                    .memberId(memId)
                    .memberPw(memPw)
                    .name(name)
                    .build();

            memberMapper.join(member);

            Integer login = memberMapper.login(member.getMemberId(), member.getMemberPw());

            if (login > 0) {
                cafeService.joinCafe(cafeId, login);

                Board board = Board.builder()
                        .memId(login)
                        .cafeId(cafeId)
                        .title("title")
                        .content("content")
                        .viewCount(0)
                        .build();

                boardMapper.writeBoard(board);
            }
        }

        memId = "zxcv";
        memPw = "1234";
        name = "hala";

        count = memberMapper.duplicateIdCheck(memId);
        if (count == 0) {
            Member member = Member.builder()
                    .memberId(memId)
                    .memberPw(memPw)
                    .name(name)
                    .build();

            memberMapper.join(member);

            Integer login = memberMapper.login(member.getMemberId(), member.getMemberPw());

            if (login > 0) {
                cafeService.joinCafe(cafeId, login);
                this.cafeId = cafeId;
                memberId = login;
            }
        }
    }

    @Test
    @DisplayName("댓글 작성")
    void writeComment() throws Exception {
        // given
        int boardId = 1;
        Comment comment = Comment.builder()
                .memId(memberId)
                .cafeId(cafeId)
                .title("comment title")
                .content("comment content")
                .reg(boardId)
                .reLevel(2)
                .reStep(0)
                .build();

        // when
        commentMapper.writeComment(comment);

        // then
        Integer count = commentMapper.countComment();
        Integer boardCount = commentMapper.countBoardComment(boardId);
        Integer memberCount = commentMapper.countMemberComment(memberId);
        Integer cafeCount = commentMapper.countCafeComment(cafeId);

        Assertions.assertThat(count).isEqualTo(1);
        Assertions.assertThat(memberCount).isEqualTo(1);
        Assertions.assertThat(boardCount).isEqualTo(1);
        Assertions.assertThat(cafeCount).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 찾기")
    void findComment() throws Exception {
        // given
        int boardId = 1;
        Comment comment = Comment.builder()
                .memId(memberId)
                .cafeId(cafeId)
                .title("comment title")
                .content("comment content")
                .reg(boardId)
                .reLevel(2)
                .reStep(1)
                .build();
        commentMapper.writeComment(comment);

        // when
        Integer id = 1;
        Comment findComm = commentMapper.findComment(id);

        // then
        Assertions.assertThat(findComm.getTitle()).isEqualTo("comment title");
        Assertions.assertThat(findComm.getContent()).isEqualTo("comment content");
    }

    @Test
    @DisplayName("댓글 수정")
    void editTest() throws Exception {
        // given
        int boardId = 1;
        Comment comment = Comment.builder()
                .memId(memberId)
                .cafeId(cafeId)
                .title("comment title")
                .content("comment content")
                .reg(boardId)
                .reLevel(2)
                .reStep(0)
                .build();
        commentMapper.writeComment(comment);

        // when
        int commId = 1;
        CommentEdit commentEdit = new CommentEdit();
        commentEdit.setTitle("title");
        commentEdit.setContent("content");
        commentMapper.updateComment(commId, commentEdit);
        Comment findComment = commentMapper.findComment(commId);

        // then
        Assertions.assertThat(findComment.getTitle()).isEqualTo("title");
        Assertions.assertThat(findComment.getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() throws Exception {
        // given
        int boardId = 1;
        Comment comment = Comment.builder()
                .memId(memberId)
                .cafeId(cafeId)
                .title("comment title")
                .content("comment content")
                .reg(boardId)
                .reLevel(2)
                .reStep(0)
                .build();
        commentMapper.writeComment(comment);

        // when
        int id = 1;
        commentMapper.deleteComment(id);
        Integer count = commentMapper.countComment();

        // then
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("board comment remove test")
    void removeBoardComment() throws Exception {
        // given
        int boardId = 1;

        for (int i = 0; i < 5; i++) {
            Comment comment = Comment.builder()
                    .memId(memberId)
                    .cafeId(cafeId)
                    .title("comment title")
                    .content("comment content")
                    .reg(boardId)
                    .reLevel(2)
                    .reStep(0)
                    .build();
            commentMapper.writeComment(comment);
        }

        // when
        commentMapper.deleteBoardComment(boardId);
        Integer count = commentMapper.countBoardComment(boardId);

        // then
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("member board comment remove test")
    void removeMemberComment() throws Exception {
        // given
        int boardId = 1;

        for (int i = 0; i < 5; i++) {
            Comment comment = Comment.builder()
                    .memId(memberId)
                    .cafeId(cafeId)
                    .title("comment title")
                    .content("comment content")
                    .reg(boardId)
                    .reLevel(2)
                    .reStep(0)
                    .build();
            commentMapper.writeComment(comment);
        }

        // when
        commentMapper.deleteMemberComment(memberId);
        Integer count = commentMapper.countMemberComment(memberId);

        // then
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("insert comment alignment test")
    void insertCommentAlignment() throws Exception {
        // given
        int boardId = 1;

        for (int i = 0; i < 5; i++) {
            Comment comment = Comment.builder()
                    .memId(memberId)
                    .cafeId(cafeId)
                    .title("comment title")
                    .content("comment content")
                    .reg(boardId)
                    .reLevel(2)
                    .reStep(1)
                    .build();
            commentMapper.insertCommentAlignment(comment.getReg(), comment.getReStep());
            commentMapper.writeComment(comment);
        }


        // when
        Integer max = commentMapper.getBoardCommentStepMax(boardId);

        // then
        Assertions.assertThat(max).isEqualTo(5);
    }

    @Test
    @DisplayName("delete comment alignment test")
    void deleteCommentAlignment() throws Exception {
        // given
        int boardId = 1;

        for (int i = 0; i < 5; i++) {
            Comment comment = Comment.builder()
                    .memId(memberId)
                    .cafeId(cafeId)
                    .title("comment title")
                    .content("comment content")
                    .reg(boardId)
                    .reLevel(2)
                    .reStep(1)
                    .build();
            commentMapper.insertCommentAlignment(comment.getReg(), comment.getReStep());
            commentMapper.writeComment(comment);
        }

        // when
        Comment comment = commentMapper.findComment(3);
        commentMapper.deleteComment(comment.getId());
        commentMapper.deleteCommentAlignment(comment.getReg(), comment.getReStep());

        Integer max = commentMapper.getBoardCommentStepMax(boardId);

        // then
        Assertions.assertThat(max).isEqualTo(4);
    }

    @Test
    @DisplayName("회원이 카페에서 탈퇴시 커페에 작성한 댓글이 삭제되는지 테스트")
    void removeMemberCafeComment() throws Exception {
        // given
        Integer boardId = 1;
        for (int i = 0; i < 10; i++) {
            Comment comment = Comment.builder()
                    .memId(memberId)
                    .cafeId(cafeId)
                    .title("title")
                    .content("content")
                    .reg(boardId)
                    .reLevel(2)
                    .reStep(1)
                    .build();
            commentMapper.writeComment(comment);
        }

        // when
        memberService.removeCafe(memberId, cafeId);

        Integer countMember = commentMapper.countMemberComment(memberId);
        Integer countCafe = commentMapper.countCafeComment(cafeId);
        Integer countBoard = commentMapper.countBoardComment(boardId);

        // then
        Assertions.assertThat(countCafe).isEqualTo(0);
        Assertions.assertThat(countBoard).isEqualTo(0);
        Assertions.assertThat(countMember).isEqualTo(0);
    }

    @Test
    @DisplayName("카파에서 회원을 탈퇴시킨후 댓글이 사라지는지 확인")
    void removeCheckCafeComment() throws Exception {
        // given
        Integer boardId = 1;
        for (int i = 0; i < 10; i++) {
            Comment comment = Comment.builder()
                    .memId(memberId)
                    .cafeId(cafeId)
                    .title("title")
                    .content("content")
                    .reg(boardId)
                    .reLevel(2)
                    .reStep(1)
                    .build();
            commentMapper.writeComment(comment);
        }

        // when
        cafeService.removeMember(cafeId, memberId);

        Integer cafeCount = commentMapper.countCafeComment(cafeId);
        Integer memberCount = commentMapper.countMemberComment(memberId);
        Integer boardCount = commentMapper.countBoardComment(boardId);

        // then
        Assertions.assertThat(cafeCount).isEqualTo(0);
        Assertions.assertThat(memberCount).isEqualTo(0);
        Assertions.assertThat(boardCount).isEqualTo(0);
    }
}