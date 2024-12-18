package com.photoChallenger.tripture.domain.profile.service;

import com.photoChallenger.tripture.domain.profile.dto.MemberDto;
import com.photoChallenger.tripture.domain.profile.dto.MemberEditForm;
import com.photoChallenger.tripture.domain.profile.dto.MemberEditResponse;

public interface ProfileService {
    //프로필 정보 확인
    MemberDto getMember(Long loginId);

    //회원 수정 폼
    MemberEditForm memberEditForm(Long loginId);

    //회원 수정
    MemberEditResponse memberEdit(String profileImgName, String profileNickname, String loginPw, Long loginId);

    //챌린저 레벨 확인
    String checkLevel(Long loginId);

    //프로필 사진유무 확인(있으면 삭제)
    String checkProfileImgName(Long loginId);

    void deleteOne(Long loginId);

    Integer getTotalPoint(Long loginId);
}
