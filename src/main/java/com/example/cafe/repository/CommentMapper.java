package com.example.cafe.repository;

import com.example.cafe.domain.Comment;
import com.example.cafe.domain.dto.CommentDto;
import com.example.cafe.domain.dto.CommentView;
import com.example.cafe.domain.edit.CommentEdit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    void writeComment(@Param("comment") Comment comment);

    Comment findComment(Integer commId);

    List<CommentView> getBoardComments(Integer boardId, Integer start, Integer end);

    void updateComment(Integer commId, @Param("commentEdit") CommentEdit commentEdit);

    // 댓글 한개 삭제
    void deleteComment(Integer commId);

    // 게시글 댓글 전체 삭제 (게시글 삭제)
    void deleteBoardComment(Integer boardId);

    // 회원 댓글 삭제 (회원 탈퇴)
    void deleteMemberComment(Integer memId);

    // 회원 탈퇴시 또는 카페 제명 처리 시 삭제
    void deleteCafeComment(Integer memId, Integer cafeId);

    Integer countComment();

    Integer countBoardComment(Integer boardId);

    Integer countMemberComment(Integer memId);

    Integer countCafeComment(Integer cafeId);

    // 댓글 작성시 오름차순 정렬
    void insertCommentAlignment(Integer reg, Integer reStep);

    // 댓글 삭제시 오름차순 정렬
    void deleteCommentAlignment(Integer reg, Integer reStep);

    Integer getBoardCommentStepMax(Integer req);
}
