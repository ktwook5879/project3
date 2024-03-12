package com.finalteam3.midam.repository;

import com.finalteam3.midam.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

   MemberEntity findByUserId(String userId);

    List<MemberEntity> findAllByOrderByUserIdxDesc();

//    고객관리 화면에서 회원이름으로 검색하기
    List<MemberEntity> findByUserNameContainingOrUserPhoneContainingOrderByUserIdxDesc(String userName, String userPhone);

//    고객관리페이지->고객디테일 페이지
    List<MemberEntity> findByUserIdx(int userIdx);

//    로그인 : 아이디 찾기
    MemberEntity findByUserNameAndUserPhone(String userName, String userPhone);

    //    로그인 : 비밀번호 찾기
    MemberEntity findByUserIdAndUserNameAndUserPhone(String userId, String userName, String userPhone);



    //  비밀번호 찾기 시 임시 비밀번호 부여
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update MemberEntity m SET m.userPwd1 = '12345' , m.userPwd2 = '12345' WHERE m.userId = :userId")
    void updateUserPwd(String userId);


    List<MemberEntity> findByUserName(String userName);

//    userEdit  메모 수정
@Transactional
@Modifying(clearAutomatically = true)
@Query("update MemberEntity m SET m.userMemo = :userMemo WHERE m.userIdx = :userIdx")
    void editMemo(int userIdx, String userMemo);


    //    고객관리에서 회원정보 리스트 받아오기(+총매출액, 최근방문일)

    @Query("SELECT m.userName AS userName, " +
            "m.gender AS gender, " +
            "m.userPhone AS userPhone, " +
            "m.userId AS userId, " +
            "m.userPwd1 AS userPwd1, " +
            "m.userPwd2 AS userPwd2, " +
            "m.createDate AS createDate, " +
            "m.adminYn AS adminYn, " +
            "m.userMemo AS userMemo, " +
            "m.userIdx AS userIdx, " +
            "MAX(ts.paymentDay) AS recentPaymentDay, " +
            "SUM(ts.payment) AS totalPayment " +
            "FROM MemberEntity m " +
            "LEFT JOIN TreatSaleEntity ts ON m.userName = ts.userName " +
            "WHERE m.adminYn='N'"+
            "GROUP BY m.userName")
    List<Object[]> getMemberSummary();

    //    고객관리에서 고객 검색 시 회원정보 리스트 받아오기(+총매출액, 최근방문일)
    @Query("SELECT m.userName AS userName, " +
            "m.gender AS gender, " +
            "m.userPhone AS userPhone, " +
            "m.userId AS userId, " +
            "m.userPwd1 AS userPwd1, " +
            "m.userPwd2 AS userPwd2, " +
            "m.createDate AS createDate, " +
            "m.adminYn AS adminYn, " +
            "m.userMemo AS userMemo, " +
            "m.userIdx AS userIdx, " +
            "MAX(ts.paymentDay) AS recentPaymentDay, " +
            "SUM(ts.payment) AS totalPayment " +
            "FROM MemberEntity m " +
            "LEFT JOIN TreatSaleEntity ts ON m.userName = ts.userName " +
            "WHERE (m.userName like %:keyword% or m.userPhone like %:keyword% ) and ( m.adminYn='N')"+
            "GROUP BY m.userName")
    List<Object[]> searchMemberDTOList(String keyword);
}
