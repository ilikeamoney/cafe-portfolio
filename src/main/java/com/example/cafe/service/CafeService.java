package com.example.cafe.service;

import com.example.cafe.domain.Board;
import com.example.cafe.domain.Cafe;
import com.example.cafe.domain.Member;
import com.example.cafe.repository.BoardMapper;
import com.example.cafe.repository.CafeMapper;
import com.example.cafe.repository.CommentMapper;
import com.example.cafe.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CafeService {

    private final MemberMapper memberMapper;

    private final BoardMapper boardMapper;

    private final CommentMapper commentMapper;

    private final CafeMapper cafeMapper;

    public void createCafe(Integer memLog, String title, String introduce, String genre, String imgName) {
        cafeMapper.createCafe(Cafe.builder()
                .adminId(memLog)
                .title(title)
                .introduce(introduce)
                .genre(genre)
                .cafeImg(imgName)
                .build());

        Integer myCafeId = cafeMapper.getMyCafeId(memLog);
        memberMapper.setMyCafeId(memLog, myCafeId);
    }

    // 카페에 가입하면 멤버테이블에 가입한 카페 아이디와 카페에는 들어온 멤버의 아이디를 셋팅
    public void joinCafe(Integer cafeId, Integer joinCrewId) {
        String myCrewId = cafeMapper.getMyCrew(cafeId);
        String joinCafeId = memberMapper.getJoinCafeId(joinCrewId);

        if (myCrewId == null) {
            myCrewId = String.valueOf(joinCrewId);
        } else {
            myCrewId = myCrewId + "," + joinCrewId;
        }

        if (joinCafeId == null) {
            joinCafeId = String.valueOf(cafeId);
        } else {
            joinCafeId = joinCafeId + "," + cafeId;
        }

        cafeMapper.setMyCrew(cafeId, myCrewId);
        memberMapper.setJoinCafeId(joinCrewId, joinCafeId);
    }

    // null 체크 해야함
    // DB에 데이터가 리스트로 반환해야하는지 아닌지를 확인
    public boolean checkDataIsList(Integer cafeId) {
        boolean check = false;
        String myCrew = cafeMapper.getMyCrew(cafeId);

        if (myCrew.contains(",")) {
            check = true;
        }

        return check;
    }

    // DB에 , 기준으로 되어있는 멤버 아이디를 리스트로 반환
    public List<Integer> getCrewId(Integer cafeId) {
        List<Integer> ids = new ArrayList<>();
        String crewId = cafeMapper.getMyCrew(cafeId);

        if (crewId != null) {
            boolean check = checkDataIsList(cafeId);

            if (check) {
                String[] str = crewId.split(",");
                for (String s : str) {
                    ids.add(Integer.parseInt(s));
                }
            } else {
                Integer id = Integer.parseInt(crewId);
                ids.add(id);
            }

            return ids;
        }
        return null;
    }

    // null 체크 해야함
    // 카페에 가입한 멤버의 게시글 전체
    public List<Board> getCafeBoards(Integer cafeId, Integer start, Integer end) {
        return boardMapper.getCafeBoard(cafeId, start, end);
    }

    public List<Board> getMemberBoards(Integer cafeId, Integer memId, Integer start, Integer end) {
        List<Integer> crewId = getCrewId(cafeId);
        boolean check = false;

        if (crewId != null) {
            for (Integer id : crewId) {
                if (id.equals(memId)) {
                    check = true;
                    break;
                }
            }

            if (check) {
                return boardMapper.getMemberBoard(memId, start, end);
            }
        }
        return null;
    }

    // null 체크 해야함
    // 카페에 가입한 멤버 정보 전체
    public List<Member> getCafeMembers(Integer cafeId) {
        List<Member> members = new ArrayList<>();
        List<Integer> crewId = getCrewId(cafeId);

        if (crewId != null) {
            for (Integer id : crewId) {
                Member member = memberMapper.find(id);
                members.add(member);
            }
        }

        return members;
    }

    // null 체크 해야함
    public void removeMember(Integer cafeId, Integer crewId) {
        List<Integer> crewIds = getCrewId(cafeId);
        List<Integer> moveId = new ArrayList<>();

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < crewIds.size(); i++) {
            if (crewIds.get(i).equals(crewId)) {
                continue;
            }
            moveId.add(crewIds.get(i));
        }

        for (int i = 0; i < moveId.size(); i++) {
            if (i < moveId.size() - 1) {
                str.append(moveId.get(i)).append(",");
            } else {
                str.append(moveId.get(i));
            }
        }

        String crewIdsStr = String.valueOf(str);
        if (crewIdsStr.isEmpty()) {
            crewIdsStr = null;
        }

        String cafeIdsStr = editJoinCafe(cafeId, crewId);
        memberMapper.setJoinCafeId(crewId, cafeIdsStr);
        cafeMapper.setMyCrew(cafeId, crewIdsStr);
        boardMapper.deleteCafeBoard(crewId, cafeId);
        commentMapper.deleteCafeComment(crewId, cafeId);
    }

    public String editJoinCafe(Integer cafeId, Integer crewId) {
        String joinCafeId = memberMapper.getJoinCafeId(crewId);
        StringBuilder str = new StringBuilder();

        if (joinCafeId.contains(",")) {
            List<Integer> moveId = new ArrayList<>();
            for (String ids : joinCafeId.split(",")) {
                 Integer id = Integer.parseInt(ids);

                 if (id.equals(cafeId)) {
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
            joinCafeId = String.valueOf(str);
        } else {
            if (cafeId.equals(Integer.parseInt(joinCafeId))) {
                joinCafeId = "";
            }
        }

        if (joinCafeId.isEmpty()) {
            return null;
        }
        return joinCafeId;
    }


    public boolean checkMyCrew(Integer cafeId, Integer crewId) {
        List<Integer> crewIds = getCrewId(cafeId);
        boolean check = false;

        if (crewIds != null) {
            for (Integer id : crewIds) {
                if (id.equals(crewId)) {
                    check = true;
                }
            }
        }

        return check;
    }

    // 파일이름 중복생성 방지 메서드
    public String createNotDuplicateFileName(String originalFileName) {
        StringBuilder str = new StringBuilder();
        if (originalFileName != null) {
            int extensionIdx = originalFileName.indexOf(".");
            String extension = originalFileName.substring(extensionIdx);
            String fileName = UUID.randomUUID().toString();

            str.append(fileName).append(extension);
        }
        return String.valueOf(str);
    }

    public boolean checkIsAdmin(Integer cafeId, Integer memLog) {
        boolean check = false;
        if (cafeMapper.getMyAdminId(cafeId).equals(memLog)) {
            check = true;
        }
        return check;
    }

    public void deleteCafe(Integer cafeId, Integer memId) {
        List<Integer> crewIds = getCrewId(cafeId);

        if (crewIds != null) {
            for (Integer crewId : crewIds) {
                removeMember(cafeId, crewId);
            }

            String myCrew = cafeMapper.getMyCrew(cafeId);
            if (myCrew == null) {
                cafeMapper.deleteCafe(cafeId);
            }
        } else {
            cafeMapper.deleteCafe(cafeId);
        }

        memberMapper.setMyCafeId(memId, null);
    }

    public void deleteCafeImg(String path) {
        File file = new File(path);
        if (file.exists()) file.delete();
    }
}
