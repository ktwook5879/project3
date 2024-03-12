package com.finalteam3.midam.service;

import com.finalteam3.midam.entity.MemberEntity;

import java.util.List;

public interface MemberService {

    //    회원가입
    void saveMember(MemberEntity member) throws Exception;

    //    로그인 회원 정보 확인
    boolean isMember(String userId, String userPwd1) throws Exception;

    //로그인 회원 정보 확인 후 정보 가져오기
    MemberEntity getMemberInfo(String userId) throws Exception;

    //    로그인 페이지에서 아이디 찾기
    MemberEntity findUserId(String userName, String userPhone);

    //    로그인 페이지에서 비밀번호 찾기
    MemberEntity findUserPwd(String userId, String userName, String userPhone);

    //    고객관리에서 회원정보 리스트 받아오기
    List<MemberEntity> selectMemberList() throws Exception;

    //    고객관리에서 회원 정보 검색하기
    List<MemberEntity> searchUser(String keyword);


    //    고객관리에서 detail페이지로 userIdx값 넘기기
    List<MemberEntity> getMemberDetail(int userIdx);


    // 비밀번호 찾기에서 데이터 있을 경우 임시비밀번호 부여하기
    void updateUserPwd(String userId);

    void updateMemberDetails(String userId, String userPhone, String userPwd1, String userPwd2);

    List<MemberEntity> searchSalesUser(String userName);

//    memberInfo에서 메모 수정하기
    void editMemo(int userIdx, String userMemo);

//    고객관리에서 회원정보 리스트 받아오기(+총매출액, 최근방문일)
    List<Object[]> getMemberDTOList();

    //     고객관리에서 고객 검색 시 검색된 회원 카드 보여주기
    List<Object[]> searchMemberDTOList(String keyword);
}
