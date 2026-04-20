package com.ams.onionuserapi.controller;

import com.ams.onioncore.dto.ApiResponse;
import com.ams.onioncore.dto.PartyVoiceEventResponse;
import com.ams.onioncore.dto.PartyVoiceJoinRequest;
import com.ams.onioncore.dto.PartyVoicePresenceResponse;
import com.ams.onioncore.dto.PushNotificationResponse;
import com.ams.onioncore.service.PartyVoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/party/{partyId}/voice")
@RequiredArgsConstructor
public class PartyVoiceController {

    private final PartyVoiceService partyVoiceService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/presence")
    public ResponseEntity<ApiResponse<List<PartyVoicePresenceResponse>>> getPresence(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId,
            @RequestParam(required = false) String channelName
    ) {
        List<PartyVoicePresenceResponse> responses =
                partyVoiceService.getActiveUsers(email, partyId, channelName);
        return ResponseEntity.ok(ApiResponse.success(responses, "음성채널 접속자 조회 성공"));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<PartyVoiceEventResponse>> join(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId,
            @Valid @RequestBody PartyVoiceJoinRequest request
    ) {
        PartyVoiceEventResponse event = partyVoiceService.join(email, partyId, request.getChannelName());
        broadcastVoiceAndNotifyMembers(event);
        return ResponseEntity.ok(ApiResponse.success(event, "음성채널 입장 성공"));
    }

    @PostMapping("/leave")
    public ResponseEntity<ApiResponse<PartyVoiceEventResponse>> leave(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long partyId
    ) {
        PartyVoiceEventResponse event = partyVoiceService.leave(email, partyId);
        broadcastVoiceAndNotifyMembers(event);
        return ResponseEntity.ok(ApiResponse.success(event, "음성채널 퇴장 성공"));
    }

    private void broadcastVoiceAndNotifyMembers(PartyVoiceEventResponse event) {
        messagingTemplate.convertAndSend(
                "/topic/party." + event.getPartyId() + ".voice",
                ApiResponse.success(event, "음성채널 상태 변경")
        );

        List<String> memberEmails = partyVoiceService.getPartyMemberEmails(event.getPartyId());
        for (String memberEmail : memberEmails) {
            if (memberEmail.equals(event.getActorEmail())) {
                continue;
            }
            messagingTemplate.convertAndSendToUser(
                    memberEmail,
                    "/queue/notifications",
                    ApiResponse.success(
                            PushNotificationResponse.builder()
                                    .type(event.getEventType())
                                    .title("음성채널 알림")
                                    .body(event.getActorNickname() + "님이 음성채널 상태를 변경했습니다.")
                                    .partyId(event.getPartyId())
                                    .actorEmail(event.getActorEmail())
                                    .actorNickname(event.getActorNickname())
                                    .createdAt(LocalDateTime.now().toString())
                                    .build(),
                            "음성채널 푸시 알림"
                    )
            );
        }
    }
}
