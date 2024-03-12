package com.finalteam3.midam.service;

import com.finalteam3.midam.entity.MemberEntity;
import com.finalteam3.midam.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//밑에서 생성자 생성해줘서 사용 x
//@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final EntityManager entityManager;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, EntityManager entityManager) {
        this.memberRepository = memberRepository;
        this.entityManager = entityManager;
    }


    //    회원가입
    @Override
    public void saveMember(MemberEntity member) throws Exception {
        memberRepository.save(member);
    }

    //    로그인 정보 일치 여부 확인
    @Override
    public boolean isMember(String userId, String userPwd1) throws Exception {
//        userId에 맞는 memberEntity 정보 불러옴
        MemberEntity memberEntity = memberRepository.findByUserId((userId));

        if (memberEntity != null & memberEntity.getUserPwd1().equals(userPwd1)) {
            return true;
        } else {
            return false;
        }
    }

    //    로그인 완료 후 일치하는 정보 받아오기
    @Override
    public MemberEntity getMemberInfo(String userId) throws Exception {
        return memberRepository.findByUserId(userId);
    }

//    로그인: 아이디 찾기

    @Override
    public MemberEntity findUserId(String userName, String userPhone) {
        return memberRepository.findByUserNameAndUserPhone(userName, userPhone);
    }

    //    로그인 : 비밀번호 찾기
    @Override
    public MemberEntity findUserPwd(String userId, String userName, String userPhone) {
        return memberRepository.findByUserIdAndUserNameAndUserPhone(userId, userName, userPhone);
    }

    //    고객관리 페이지 : 고객리스트 받아오기
//    @Override
//    public List<MemberEntity> selectMemberList() throws Exception {
//        return memberRepository.findAllByOrderByUserIdxDesc();
//
//    }

    //    고객관리 페이지 : 고객리스트 받아오기(관리자 제외하고 받아오기)


    @Override
    public List<MemberEntity> selectMemberList() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MemberEntity> criteriaQuery = criteriaBuilder.createQuery(MemberEntity.class);
        Root<MemberEntity> root = criteriaQuery.from(MemberEntity.class);

        Predicate predicate = criteriaBuilder.equal(root.get("adminYn"), "N");
        criteriaQuery.where(predicate);

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("userIdx")));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
    //    고객관리 페이지 : 고객리스트 받아오기(관리자 제외하고 받아오기) 끝



    //    고객관리페이지 : 고객검색하기
    @Override
    public List<MemberEntity> searchUser(String keyword) {
        return memberRepository.findByUserNameContainingOrUserPhoneContainingOrderByUserIdxDesc(keyword, keyword);
    }

    //    고객관리페이지 -> 디테일 페이지로 이동하기
    @Override
    public List<MemberEntity> getMemberDetail(int userIdx) {
        return memberRepository.findByUserIdx(userIdx);
    }

    @Override
    public void updateUserPwd(String userId) {
        memberRepository.updateUserPwd(userId);
    }

    @Override
    public void updateMemberDetails(String userId, String userPhone, String userPwd1, String userPwd2) {
        MemberEntity member = memberRepository.findByUserId(userId);
        if (member != null) {
            if (userPhone != null) {
                member.setUserPhone(userPhone);
            }
            if (userPwd1 != null) {
                member.setUserPwd1(userPwd1);
            }
            if (userPwd2 != null) {
                member.setUserPwd2(userPwd2);
            }
            memberRepository.save(member);
        }
    }

    @Override
    public List<MemberEntity> searchSalesUser(String userName) {

        return  memberRepository.findByUserName(userName);
    }

    @Override
    public void editMemo(int userIdx, String userMemo) {
        memberRepository.editMemo(userIdx, userMemo);
    }

    //    고객관리에서 회원정보 리스트 받아오기(+총매출액, 최근방문일)

    @Override
    public List<Object[]> getMemberDTOList() {
        return memberRepository.getMemberSummary();
    }

    //    고객검색하기
    @Override
    public List<Object[]> searchMemberDTOList(String keyword) {
        return memberRepository.searchMemberDTOList(keyword);
    }
}

