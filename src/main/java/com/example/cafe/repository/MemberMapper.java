package com.example.cafe.repository;

import com.example.cafe.domain.Member;
import com.example.cafe.domain.edit.MemberEdit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    // 로그인
    Integer login(String memberId, String memberPw);

    void setMyCafeId(Integer id, Integer cafeId);

    // 가입
    void join(Member member);

    // 아이디 중복 체크
    Integer duplicateIdCheck(String memberId);

    // 회원 찾기
    Member find(int id);

    // 회원 수정
    void update(int id, @Param("memberEdit") MemberEdit memberEdit);

    // 회원 삭제
    void delete(int id);

    // 전체 회원 수
    Integer getTotal();

    // 가입한 카페
    String getJoinCafeId(Integer memId);

    void setJoinCafeId(Integer memId, String joinCafeId);
}
