package com.example.cafe.repository;

import com.example.cafe.domain.Cafe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CafeMapper {

    // 카페 생성
    void createCafe(@Param("cafe") Cafe cafe);

    // 카페 찾는 메소드
    Cafe find(Integer cafeId);

    // 카페 주인이 자기 카페 찾는 메소드
    Cafe findMyCafe(Integer adminId);

    // 카페에 가입한 회원 아이디 가져오는 함수
    String getMyCrew(Integer cafeId);

    List<Cafe> getAllCafe(Integer start, Integer end);

    // 카페 멤버 아이디 다시 셋팅
    void setMyCrew(Integer cafeId, String myCrew);

    // 카페 주인 아이디를 넣으면 카페 아이디 찾는 메소드
    Integer getMyCafeId(Integer adminId);

    // 카페 아이디를 넣으면 카페 주인 아이디 반환하는 메소드
    Integer getMyAdminId(Integer cafeId);

    Integer getTotalCafeCount();

    Integer getCafeBoardCount(Integer cafeId);

    void deleteCafe(Integer cafeId);
}
