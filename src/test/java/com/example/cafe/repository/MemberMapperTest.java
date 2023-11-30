package com.example.cafe.repository;

import com.example.cafe.domain.Cafe;
import com.example.cafe.domain.Member;
import com.example.cafe.domain.edit.MemberEdit;
import com.example.cafe.service.CafeService;
import com.example.cafe.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MemberMapperTest {

    @Autowired
    MemberMapper memberMapper;

    @Test
    @DisplayName("회원가입")
    void join() throws Exception {
        // given
        Member member = Member.builder()
                .memberId("qwer")
                .memberPw("1234")
                .name("james")
                .build();

        // when
        memberMapper.join(member);

        // then
        Assertions.assertThat(memberMapper.getTotal()).isGreaterThan(0);
    }


    @Test
    @DisplayName("수정 조회")
    void update() throws Exception {
        // given
        Member member = Member.builder()
                .memberId("qwer")
                .memberPw("1234")
                .name("james")
                .build();
        memberMapper.join(member);

        // when
        int id = 1;
        MemberEdit memberEdit = new MemberEdit();
        memberEdit.setMemberId("zxcv");
        memberEdit.setMemberPw("5678");
        memberEdit.setName("kevin");
        memberMapper.update(id, memberEdit);

        // then
        Member findMember = memberMapper.find(id);
        Assertions.assertThat(findMember.getMemberId())
                .isEqualTo("zxcv");
    }

    @Test
    @DisplayName("삭제")
    void delete() throws Exception {
        // given
        Member member = Member.builder()
                .memberId("qwer")
                .memberPw("1234")
                .name("james")
                .build();
        memberMapper.join(member);

        // when
        int id = 1;
        memberMapper.delete(id);

        // then
        Assertions.assertThat(memberMapper.getTotal()).isEqualTo(0);
    }

    @Test
    @DisplayName("로그인")
    void loginTest() throws Exception {
        // given
        Member member = Member.builder()
                .memberId("qwer")
                .memberPw("1234")
                .name("james")
                .build();
        memberMapper.join(member);

        // when
        int login = memberMapper.login(member.getMemberId(), member.getMemberPw());

        // then
        Assertions.assertThat(login).isEqualTo(1);
    }

    @Test
    @DisplayName("아이디 중복 체크")
    void duplicateCheck() throws Exception {
        // given
        Member member = Member.builder()
                .memberId("qwer")
                .memberPw("1234")
                .name("james")
                .build();
        memberMapper.join(member);

        // when
        int count = memberMapper.duplicateIdCheck(member.getMemberId());

        // then
        Assertions.assertThat(count).isGreaterThan(0);
    }
}