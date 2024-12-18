package com.photoChallenger.tripture.domain.purchase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.photoChallenger.tripture.domain.login.dto.LoginIdResponse;
import com.photoChallenger.tripture.domain.login.entity.SessionConst;
import com.photoChallenger.tripture.domain.purchase.dto.*;
import com.photoChallenger.tripture.domain.purchase.entity.SessionUtils;
import com.photoChallenger.tripture.domain.purchase.service.PurchaseService;
import com.photoChallenger.tripture.global.exception.TriptureException;
import com.photoChallenger.tripture.global.exception.purchase.KakaoPayCancelException;
import com.photoChallenger.tripture.global.exception.purchase.KakaoPayFailException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    //사용 전 아이템 리스트
    @GetMapping("/ItemsBeforeUse")
    public ResponseEntity<List<PurchaseItemResponse>> checkItemsBeforeUse(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return ResponseEntity.ok().body(purchaseService.checkItemsBeforeUse(loginIdResponse.getLoginId()));
    }

    //사용 후 아이템 리스트
    @GetMapping("/ItemsAfterUse")
    public ResponseEntity<List<PurchaseItemResponse>> checkItemsAfterUse(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return ResponseEntity.ok().body(purchaseService.checkItemsAfterUse(loginIdResponse.getLoginId()));
    }

    //아이템 상세정보 및 구매정보 확인
    @GetMapping("/{purchaseId}/detail")
    public ResponseEntity<PurchaseItemDto> checkDetail(HttpServletRequest request, @PathVariable("purchaseId") Long purchaseId){
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return ResponseEntity.ok().body(purchaseService.checkDetail(purchaseId));
    }

    //아이템 사용하기
    @PostMapping("/{purchaseId}/use")
    public ResponseEntity<String> useTicket(HttpServletRequest request, @PathVariable("purchaseId") Long purchaseId){
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);
        purchaseService.useItem(purchaseId);
        return new ResponseEntity("redirection request", HttpStatus.SEE_OTHER);
    }

    //포인트 사용
    @PostMapping("/order/pay")
    public ResponseEntity<String> pay(HttpServletRequest request, @RequestBody PayInfoDto payInfoDto) {
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);

        purchaseService.pointPayment(payInfoDto, loginIdResponse.getLoginId());
        return ResponseEntity.ok().body("Payment completed");
    }

    /**
     *  카카오페이 결제 요청
     */
    /**
    @PostMapping("/order/pay")
    public ResponseEntity<KakaoPayResponse> payReady(HttpServletRequest request, @RequestBody PayInfoDto payInfoDto) throws JsonProcessingException {
        HttpSession session = request.getSession(false);
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 카카오 결제 준비하기	- 결제요청 service 실행.
        KakaoPayResponse kakaoPayResponse = purchaseService.kakaoPayReady(payInfoDto, loginIdResponse.getLoginId());
        return ResponseEntity.ok().body(kakaoPayResponse); // 클라이언트에 보냄.(tid,next_redirect_pc_url이 담겨있음.)
    }
    */

    /**
     * 카카오페이 결제 완료
     */

    /**
     *
    @GetMapping("/payment/success")
    public ResponseEntity<String> payCompleted(@RequestParam("pg_token") String pgToken, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        KakaoPaySessionDto kakaoPaySessionDto = (KakaoPaySessionDto) session.getAttribute("kakaoPaySession");
        LoginIdResponse loginIdResponse = (LoginIdResponse) session.getAttribute(SessionConst.LOGIN_MEMBER);

        ApproveResponse approveResponse = purchaseService.payApprove(loginIdResponse.getLoginId(),kakaoPaySessionDto, pgToken);
        return ResponseEntity.ok().body("kakaoPay success");
    }
    @GetMapping("payment/cancel")
    public ResponseEntity<String> payCancel(){
        throw new KakaoPayCancelException();
    }
    @GetMapping("/paymeny/fail")
    public ResponseEntity<String> payFail(){
        throw new KakaoPayFailException();
    }
    */
}
