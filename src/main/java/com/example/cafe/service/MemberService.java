package com.example.cafe.service;

import com.example.cafe.domain.Cafe;
import com.example.cafe.domain.Member;
import com.example.cafe.domain.form.MemberJoinForm;
import com.example.cafe.domain.form.MemberLoginForm;
import com.example.cafe.repository.BoardMapper;
import com.example.cafe.repository.CafeMapper;
import com.example.cafe.repository.CommentMapper;
import com.example.cafe.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberMapper memberMapper;
    private final CafeMapper cafeMapper;
    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;

    public void join(MemberJoinForm memberForm) {
        memberMapper.join(Member.builder()
                .memberId(memberForm.getMemberId())
                .memberPw(memberForm.getMemberPw())
                .name(memberForm.getName())
                .build());
    }

    public Integer login(MemberLoginForm memberLoginForm) {
        return memberMapper.login(memberLoginForm.getMemberId(), memberLoginForm.getMemberPw());
    }

    // 데이터가 리스트로 반환할지 그냥 반환할지 확인
    public boolean checkDataIsList(Integer memId) {
        boolean check = false;
        String joinCafeId = memberMapper.getJoinCafeId(memId);

        if (joinCafeId.contains(",")) {
            check = true;
        }
        return check;
    }

    // 회원이 가입한 카페의 아이디를 리스트로 반환
    public List<Integer> getJoinCafeId(Integer memId) {
        List<Integer> ids = new ArrayList<>();
        String cafeId = memberMapper.getJoinCafeId(memId);

        // 값이 null 이 아니면 리스트 반환
        if (cafeId != null) {
            boolean check = checkDataIsList(memId);

            if (check) {
                String[] str = cafeId.split(",");
                for (String s : str) {
                    ids.add(Integer.parseInt(s));
                }
            } else {
                Integer id = Integer.parseInt(cafeId);
                ids.add(id);
            }
            return ids;
        }
        return null;
    }

    // null 체크 해야함
    // 가입한 카페 아이디를 가지고 찾아서 객체리스트로 돌려주는 함수
    public List<Cafe> getJoinCafeList(Integer memId) {
        List<Cafe> cafes = new ArrayList<>();

        // 가입하지 않으면 null 을 반환해서 null 체크
        if (getJoinCafeId(memId) != null) {
            for (Integer id : getJoinCafeId(memId)) {
                Cafe cafe = cafeMapper.find(id);
                cafes.add(cafe);
            }
            return cafes;
        }

        return null;
    }

    // null 체크 해야함
    public void removeCafe(Integer memId, Integer cafeId) {
        List<Integer> joinCafeId = getJoinCafeId(memId);
        List<Integer> moveId = new ArrayList<>();

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < joinCafeId.size(); i++) {
            if (joinCafeId.get(i).equals(cafeId)) {
                continue;
            }
            moveId.add(joinCafeId.get(i));
        }

        for (int i = 0; i < moveId.size(); i++) {
            if (i < moveId.size() - 1) {
                str.append(moveId.get(i)).append(",");
            } else {
                str.append(moveId.get(i));
            }
        }

        String ids = String.valueOf(str);
        if (ids.isEmpty()) {
            ids = null;
        }

        String crewIdsStr = editCafeCrewId(memId, cafeId);
        memberMapper.setJoinCafeId(memId, ids);
        cafeMapper.setMyCrew(cafeId, crewIdsStr);
        boardMapper.deleteCafeBoard(memId, cafeId);
        commentMapper.deleteCafeComment(memId, cafeId);
    }

    public String editCafeCrewId(Integer memId, Integer cafeId) {
        String cafeCrewId = cafeMapper.getMyCrew(cafeId);
        StringBuilder str = new StringBuilder();

        if (cafeCrewId.contains(",")) {
            List<Integer> moveId = new ArrayList<>();
            for (String ids : cafeCrewId.split(",")) {
                Integer id = Integer.parseInt(ids);

                if (id.equals(memId)) {
                    continue;
                }
                moveId.add(id);
            }

            for (int i = 0; i < moveId.size(); i++) {
                if (i < moveId.size() - 1) {
                    str.append(moveId.get(i)).append(",");
                } else {
                    str.append(moveId.get(i));
                }
            }
            cafeCrewId = String.valueOf(str);
        } else {
            if (memId.equals(Integer.parseInt(cafeCrewId))) {
                cafeCrewId = "";
            }
        }

        if (cafeCrewId.isEmpty()) {
            return null;
        }
        return cafeCrewId;
    }

    public boolean checkMyJoinCafe(Integer memId, Integer cafeId) {
        List<Integer> joinCafeId = getJoinCafeId(memId);
        boolean check = false;

        if (joinCafeId != null) {
            for (Integer id : joinCafeId) {
                if (id.equals(cafeId)) {
                    check = true;
                }
            }
        }
        return check;
    }

    public Integer isExistCafeId(Integer memId) {
        Member findMember = memberMapper.find(memId);
        Integer myCafeId = findMember.getMyCafeId();

        if (myCafeId != null) {
            return myCafeId;
        }
        return null;
    }
}
