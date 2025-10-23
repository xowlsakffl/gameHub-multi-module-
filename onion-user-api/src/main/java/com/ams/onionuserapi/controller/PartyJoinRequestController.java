package com.ams.onionuserapi.controller;

import com.ams.onioncore.dto.ApiResponse;
import com.ams.onioncore.dto.JoinRequest;
import com.ams.onioncore.dto.JoinResponse;
import com.ams.onioncore.service.PartyJoinRequestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/party")
@RequiredArgsConstructor
public class PartyJoinRequestController {

    private final PartyJoinRequestService partyJoinRequestService;
    private final Logger log = LoggerFactory.getLogger(PartyJoinRequestController.class);

    /** 자동 참가 **/
    @PostMapping("/{partyId}/auto-join")
    public ResponseEntity<ApiResponse<Void>> autoJoin(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId
    ) {
        partyJoinRequestService.autoJoin(email, partyId);
        return ResponseEntity.ok(ApiResponse.successMessage("자동 참가 성공"));
    }

    /** 참가 요청 생성 **/
    @PostMapping("/{partyId}/join")
    public ResponseEntity<ApiResponse<JoinResponse>> requestJoin(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId,
            @RequestBody JoinRequest request
    ) {
        log.info("참가 요청 by user: {}, partyId: {}", email, partyId);

        // 요청 DTO에 partyId 세팅
        request.setPartyId(partyId);

        JoinResponse response = partyJoinRequestService.requestJoin(
                email,
                request
        );

        return ResponseEntity.ok(ApiResponse.success(response, "파티 참가 요청 성공"));
    }

    /** 참가 요청 승인(방장 전용) **/
    @PostMapping("/join/{requestId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveRequest(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long requestId
    ) {
        partyJoinRequestService.approveRequest(email, requestId);
        return ResponseEntity.ok(ApiResponse.successMessage("참가 요청 승인 성공"));
    }


    /** 참가 요청 거절(방장 전용) */
    @PostMapping("/join/{requestId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectRequest(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long requestId
    ) {
        partyJoinRequestService.rejectRequest(email, requestId);
        return ResponseEntity.ok(ApiResponse.successMessage("참가 요청 거절 성공"));
    }

    /** 대기 중 참가 요청 조회 (방장 전용) */
    @GetMapping("/{partyId}/join/pending")
    public ResponseEntity<ApiResponse<List<JoinResponse>>> getPendingRequests(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId
    ) {
        List<JoinResponse> responses = partyJoinRequestService.getPendingRequests(email, partyId)
                .stream().map(JoinResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(responses, "대기 중 참가 요청 조회 성공"));
    }
}
