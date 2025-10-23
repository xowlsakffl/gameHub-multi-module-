package com.ams.onionuserapi.controller;

import com.ams.onioncore.dto.ApiResponse;
import com.ams.onioncore.dto.GamePartyResponse;
import com.ams.onioncore.dto.PartyMemberResponse;
import com.ams.onioncore.service.PartyMemberService;
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
public class PartyMemberController {

    private final PartyMemberService partyMemberService;
    private final Logger log = LoggerFactory.getLogger(PartyMemberController.class);

    @PostMapping("/{partyId}/leave")
    public ResponseEntity<ApiResponse<Void>> leaveParty(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId
    ) {
        log.info("파티 나가기 요청 - user: {}, partyId: {}", email, partyId);
        partyMemberService.leaveParty(email, partyId);
        return ResponseEntity.ok(ApiResponse.successMessage("파티 나가기 성공"));
    }

    @DeleteMapping("/{partyId}/member/{memberId}")
    public ResponseEntity<ApiResponse<Void>> kickMember(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId,
            @PathVariable Long memberId
    ) {
        log.info("멤버 강퇴 요청 - leader: {}, partyId: {}, targetMemberId: {}", email, partyId, memberId);
        partyMemberService.kickMember(email, partyId, memberId);
        return ResponseEntity.ok(ApiResponse.successMessage("파티 멤버 강퇴 성공"));
    }

    @PatchMapping("/{partyId}/delegate/{newLeaderId}")
    public ResponseEntity<ApiResponse<Void>> delegateLeader(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId,
            @PathVariable Long newLeaderId
    ) {
        log.info("방장 위임 요청 - from: {}, partyId: {}, to: {}", email, partyId, newLeaderId);
        partyMemberService.delegateLeader(email, partyId, newLeaderId);
        return ResponseEntity.ok(ApiResponse.successMessage("방장 위임 성공"));
    }

    /** 특정 파티 멤버 목록 조회 */
    @GetMapping("/{partyId}/members")
    public ResponseEntity<ApiResponse<List<PartyMemberResponse>>> getMembersByParty(
            @PathVariable Long partyId
    ) {
        List<PartyMemberResponse> members = partyMemberService.getMembersByParty(partyId)
                .stream().map(PartyMemberResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(members, "파티 멤버 목록 조회 성공"));
    }

    /** 내가 속한 파티 목록 조회 */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<GamePartyResponse>>> getMyParties(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        List<GamePartyResponse> parties = partyMemberService.getMyParties(email)
                .stream().map(GamePartyResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(parties, "내가 속한 파티 목록 조회 성공"));
    }
}