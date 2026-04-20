package com.ams.onionuserapi.controller;

import com.ams.onioncore.dto.ApiResponse;
import com.ams.onioncore.dto.PartyChatMessageResponse;
import com.ams.onioncore.dto.PartyChatReadRequest;
import com.ams.onioncore.dto.PartyChatReadResponse;
import com.ams.onioncore.service.PartyChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/party/{partyId}/chat")
@RequiredArgsConstructor
public class PartyChatController {

    private final PartyChatService partyChatService;

    @GetMapping("/messages")
    public ResponseEntity<ApiResponse<List<PartyChatMessageResponse>>> getMessages(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId,
            @RequestParam(required = false) Long beforeMessageId,
            @RequestParam(defaultValue = "50") int limit
    ) {
        List<PartyChatMessageResponse> messages =
                partyChatService.getMessages(email, partyId, beforeMessageId, limit);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    @PatchMapping("/read")
    public ResponseEntity<ApiResponse<PartyChatReadResponse>> markRead(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId,
            @Valid @RequestBody PartyChatReadRequest request
    ) {
        PartyChatReadResponse response =
                partyChatService.markRead(email, partyId, request.getLastReadMessageId());
        return ResponseEntity.ok(ApiResponse.success(response, "읽음 상태가 업데이트되었습니다."));
    }
}
