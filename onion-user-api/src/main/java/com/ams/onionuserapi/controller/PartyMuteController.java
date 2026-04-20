package com.ams.onionuserapi.controller;

import com.ams.onioncore.dto.ApiResponse;
import com.ams.onioncore.dto.PartyMuteRequest;
import com.ams.onioncore.dto.PartyMuteResponse;
import com.ams.onioncore.service.PartyMuteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/party/{partyId}/mute")
@RequiredArgsConstructor
public class PartyMuteController {

    private final PartyMuteService partyMuteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PartyMuteResponse>>> getActiveMutes(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId
    ) {
        List<PartyMuteResponse> responses = partyMuteService.getActiveMutes(email, partyId);
        return ResponseEntity.ok(ApiResponse.success(responses, "뮤트 목록 조회 성공"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PartyMuteResponse>> mute(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId,
            @Valid @RequestBody PartyMuteRequest request
    ) {
        PartyMuteResponse response = partyMuteService.mute(
                email,
                partyId,
                request.getUserId(),
                request.getDurationMinutes(),
                request.getReason()
        );
        return ResponseEntity.ok(ApiResponse.success(response, "뮤트 처리 성공"));
    }

    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<ApiResponse<Void>> unmute(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId,
            @PathVariable Long targetUserId
    ) {
        partyMuteService.unmute(email, partyId, targetUserId);
        return ResponseEntity.ok(ApiResponse.successMessage("뮤트 해제 성공"));
    }
}
