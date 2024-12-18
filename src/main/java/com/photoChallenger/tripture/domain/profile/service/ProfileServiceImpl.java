package com.photoChallenger.tripture.domain.profile.service;

import com.photoChallenger.tripture.domain.comment.entity.Comment;
import com.photoChallenger.tripture.domain.comment.repository.CommentRepository;
import com.photoChallenger.tripture.domain.login.entity.Login;
import com.photoChallenger.tripture.domain.login.entity.LoginType;
import com.photoChallenger.tripture.domain.login.repository.LoginRepository;
import com.photoChallenger.tripture.domain.post.entity.Post;
import com.photoChallenger.tripture.domain.post.repository.PostRepository;
import com.photoChallenger.tripture.domain.postLike.entity.PostLike;
import com.photoChallenger.tripture.domain.postLike.repository.PostLikeRepository;
import com.photoChallenger.tripture.domain.profile.dto.MemberDto;
import com.photoChallenger.tripture.domain.profile.dto.MemberEditForm;
import com.photoChallenger.tripture.domain.profile.dto.MemberEditRequest;
import com.photoChallenger.tripture.domain.profile.dto.MemberEditResponse;
import com.photoChallenger.tripture.domain.profile.entity.Profile;
import com.photoChallenger.tripture.domain.profile.repository.ProfileRepository;
import com.photoChallenger.tripture.global.S3.S3Service;
import com.photoChallenger.tripture.global.exception.global.S3IOException;
import com.photoChallenger.tripture.global.exception.login.NoSuchLoginException;
import com.photoChallenger.tripture.global.exception.profile.DuplicateNicknameException;
import com.photoChallenger.tripture.global.redis.RedisDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService{
    private final LoginRepository loginRepository;
    private final ProfileRepository profileRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final RedisDao redisDao;
    private final S3Service s3Service;

    @Override
    public MemberDto getMember(Long LoginId){
        Login login = loginRepository.findById(LoginId).get();
        return MemberDto.from(login);
    }

    @Override
    public MemberEditForm memberEditForm(Long loginId) {
        Login login = loginRepository.findById(loginId).get();
        return MemberEditForm.from(login);
    }

    @Override
    @Transactional
    public MemberEditResponse memberEdit(String profileImgName, String profileNickname, String loginPw, Long loginId) {
        Login login = loginRepository.findById(loginId).get();
        Profile profile = login.getProfile();
        //닉네임 중복 확인
//        if(!profileNickname.equals(profile.getProfileNickname())
//                && profileRepository.existsByProfileNickname(profileNickname)) {
//            throw new DuplicateNicknameException();
//        }

        if(profileImgName.equals("default") || profileImgName.isEmpty() || profileImgName == null) {profileImgName = profile.getProfileImgName();}
        if(profileNickname.isEmpty() || profileNickname == null) {profileNickname = profile.getProfileNickname();}

        profile.update(profileImgName,profileNickname);
        if (loginPw!=null && !loginPw.isEmpty() && login.getLoginType().equals(LoginType.SELF)) {
            login.update(loginPw);
        }

        return MemberEditResponse.of(profileNickname,profileImgName);
    }

    @Override
    public String checkLevel(Long loginId) {
        Login login = loginRepository.findById(loginId).get();
        return login.getProfile().getProfileLevel().getDescription();
    }

    @Override
    public String checkProfileImgName(Long loginId) {
        Login login = loginRepository.findById(loginId).get();
        return !login.getProfile().getProfileImgName().equals("default")? login.getProfile().getProfileImgName():null;
    }

    @Override
    @Transactional
    public void deleteOne(Long loginId) throws NoSuchElementException{
        Login login = loginRepository.findById(loginId).get();
        Profile p = login.getProfile();
//        Profile p = profileRepository.findAllByProfileId(login.getProfile().getProfileId());

        //이미지 삭제
        String checkProfileImgName = checkProfileImgName(loginId);
        try{
            if (checkProfileImgName != null){
                s3Service.delete(checkProfileImgName);
            }
        } catch (IOException e){
            throw new S3IOException();
        }

        //댓글 삭제
        commentRepository.deleteByProfileId(p.getProfileId());
        commentRepository.deleteByCommentGroupId(p.getProfileId());
        //포토챌린지 좋아요한 내역 불러와 좋아요수 -1 진행
        List<PostLike> postLikeList = postLikeRepository.findWhereILike(p.getProfileId());
        for(PostLike postLike : postLikeList){
            postLike.getPost().subtractLikeCount();
        }
        //포토챌린지 좋아요 삭제
        postLikeRepository.deleteByProfileId(p.getProfileId());

        profileRepository.deleteById(login.getProfile().getProfileId());
    }

    @Override
    public Integer getTotalPoint(Long loginId) {
        Login login = loginRepository.findById(loginId).orElseThrow(NoSuchLoginException::new);
        return login.getProfile().getProfileTotalPoint();
    }
}
