package com.photoChallenger.tripture.domain.bookmark.controller;

import com.photoChallenger.tripture.domain.bookmark.dto.MyContentResponse;
import com.photoChallenger.tripture.domain.bookmark.dto.MyPhotoChallengeResponse;
import com.photoChallenger.tripture.domain.bookmark.service.BookmarkService;
import com.photoChallenger.tripture.domain.login.dto.LoginIdResponse;
import com.photoChallenger.tripture.domain.login.entity.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    //최신순으로 정렬된 관광지ID 리스트
    @GetMapping("/contents")
    public ResponseEntity<List<MyContentResponse>> getOrderByContentsASC(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return ResponseEntity.ok().body(bookmarkService.getContentList(loginIdResponse.getLoginId()));
    }

    //최신순으로 정렬된 내가 저장한 챌린지 리스트
    @GetMapping("/photoChallenges")
    public ResponseEntity<List<MyPhotoChallengeResponse>> getOrderByPhotoChallengesASC(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return ResponseEntity.ok().body(bookmarkService.getPhotoChallengeList(loginIdResponse.getLoginId()));
    }
}
