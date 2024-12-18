package com.photoChallenger.tripture.domain.bookmark.controller;

import com.photoChallenger.tripture.domain.bookmark.dto.*;
import com.photoChallenger.tripture.domain.bookmark.service.BookmarkService;
import com.photoChallenger.tripture.domain.login.dto.LoginIdResponse;
import com.photoChallenger.tripture.domain.login.entity.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    //최신순으로 정렬된 관광지ID 리스트
    @GetMapping("/contents")
    public ResponseEntity<MyContentListResponse> getOrderByContents(HttpServletRequest request,
                                                                    @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) throws IOException {
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return ResponseEntity.ok().body(bookmarkService.getContentList(loginIdResponse.getLoginId(), pageNo));
    }

    //최신순으로 정렬된 내가 저장한 챌린지 리스트
    @GetMapping("/photoChallenges")
    public ResponseEntity<MyPhotoChallengeListResponse> getOrderByPhotoChallenges(HttpServletRequest request,
                                                                                  @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) throws IOException{
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return ResponseEntity.ok().body(bookmarkService.getPhotoChallengeList(loginIdResponse.getLoginId(),pageNo));
    }

    //북마크 저장 (포토챌린지)
    @PostMapping("/save/photochallenge/{postId}")
    public ResponseEntity<BookmarkSaveResponse> savePhotoChallengeBookmark(@PathVariable Long postId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);

        return ResponseEntity.ok().body(bookmarkService.savePhotoChallengeBookmark(postId, loginIdResponse.getLoginId()));
    }

    //북마크 저장 (관광지)
    @PostMapping("/save/content/{contentId}")
    public ResponseEntity<String> saveContentBookmark(@PathVariable String contentId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);

        return ResponseEntity.ok().body(bookmarkService.saveContentIdBookmark(contentId, loginIdResponse.getLoginId()));
    }

    //관광지 북마크 저장 여부
    @GetMapping("check/content/{contentId}")
    public ResponseEntity<Boolean> checkContentBookmark(HttpServletRequest request, @PathVariable String contentId) {
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);

        return ResponseEntity.ok().body(bookmarkService.checkContentBookmark(loginIdResponse.getLoginId(), contentId));
    }

}
