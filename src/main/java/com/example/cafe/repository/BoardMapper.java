package com.example.cafe.repository;

import com.example.cafe.domain.Board;
import com.example.cafe.domain.dto.BoardDto;
import com.example.cafe.domain.edit.BoardEdit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    void writeBoard(@Param("board") Board board);
    Board findBoard(Integer boardId);
    BoardDto findBoardDetail(Integer boardId);
    void updateBoard(@Param("boardEdit") BoardEdit boardEdit);
    List<Board> getMemberBoard(Integer memId, Integer start, Integer end);
    List<Board> getCafeBoard(Integer cafeId, Integer start, Integer end);
    // 게시글 삭제
    void deleteBoard(Integer boardId);
    // 회원탈퇴시 작성한 모든 게시글 삭제
    void deleteAllMylBoard(Integer memId);
    void deleteCafeBoard(Integer memId, Integer cafeId);
    Integer countCafeBoard(Integer cafeId);
    Integer countMyBoard(Integer memId);
    Integer countAllBoard();
    void increaseViewCount(Integer boardId);
}
