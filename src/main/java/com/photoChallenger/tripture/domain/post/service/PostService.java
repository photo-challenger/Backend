package com.photoChallenger.tripture.domain.post.service;

import com.photoChallenger.tripture.domain.post.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    MyPostListResponse findMyPosts(Long loginId, int pageNo);

     GetPostResponse getPost(Long postId, Long loginId);

     void editPost(Long postId, String postContent) throws IOException;

     String deletePost(Long postId) throws IOException;

     SearchListResponse searchPost(String searchOne, int pageNo);

    PopularPostListResponse popularPostList(Long loginId, int pageNo);

    List<ChallengePopularPostResponse> getPopularPost10(Long loginId, String properties);

    void newPost(Long loginId, String postContent, MultipartFile file, String contentId, String areaCode, String postChallengeName);

    boolean checkPostExists(Long loginId, String contentId);
}
