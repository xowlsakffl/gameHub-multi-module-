package com.ams.onionuserapi.controller;

import com.ams.onioncore.dto.ApiResponse;
import com.ams.onioncore.dto.GamePartyResponse;
import com.ams.onioncore.dto.PartyInviteCodeResponse;
import com.ams.onioncore.dto.PartyMemberResponse;
import com.ams.onioncore.dto.PartyRoleUpdateRequest;
import com.ams.onioncore.service.PartyMemberService;
import jakarta.validation.Valid;
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
        log.info("멤버 강퇴 요청 - actor: {}, partyId: {}, targetMemberId: {}", email, partyId, memberId);
        partyMemberService.kickMember(email, partyId, memberId);
        return ResponseEntity.ok(ApiResponse.successMessage("파티 멤버 강퇴 성공"));
    }

    @PatchMapping("/{partyId}/delegate/{newLeaderId}")
    public ResponseEntity<ApiResponse<Void>> delegateLeader(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId,
            @PathVariable Long newLeaderId
    ) {
        partyMemberService.delegateLeader(email, partyId, newLeaderId);
        return ResponseEntity.ok(ApiResponse.successMessage("방장 위임 성공"));
    }

    @PatchMapping("/{partyId}/role")
    public ResponseEntity<ApiResponse<Void>> updateRole(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId,
            @Valid @RequestBody PartyRoleUpdateRequest request
    ) {
        partyMemberService.updateRole(email, partyId, request.getUserId(), request.getRole());
        return ResponseEntity.ok(ApiResponse.successMessage("멤버 권한 변경 성공"));
    }

    @GetMapping("/{partyId}/members")
    public ResponseEntity<ApiResponse<List<PartyMemberResponse>>> getMembersByParty(
            @PathVariable Long partyId
    ) {
        List<PartyMemberResponse> members = partyMemberService.getMembersByParty(partyId)
                .stream().map(PartyMemberResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(members, "파티 멤버 목록 조회 성공"));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<GamePartyResponse>>> getMyParties(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        List<GamePartyResponse> parties = partyMemberService.getMyParties(email)
                .stream().map(GamePartyResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(parties, "내 파티 목록 조회 성공"));
    }

    @GetMapping("/{partyId}/invite-code")
    public ResponseEntity<ApiResponse<PartyInviteCodeResponse>> getInviteCode(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId
    ) {
        PartyInviteCodeResponse response = partyMemberService.getInviteCode(email, partyId);
        return ResponseEntity.ok(ApiResponse.success(response, "초대코드 조회 성공"));
    }

    @PostMapping("/{partyId}/invite-code/regenerate")
    public ResponseEntity<ApiResponse<PartyInviteCodeResponse>> regenerateInviteCode(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId
    ) {
        PartyInviteCodeResponse response = partyMemberService.regenerateInviteCode(email, partyId);
        return ResponseEntity.ok(ApiResponse.success(response, "초대코드 재발급 성공"));
    }

    @PostMapping("/invite/{inviteCode}/join")
    public ResponseEntity<ApiResponse<Void>> joinByInviteCode(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable String inviteCode
    ) {
        partyMemberService.joinByInviteCode(email, inviteCode);
        return ResponseEntity.ok(ApiResponse.successMessage("초대코드 입장 성공"));
    }
}
