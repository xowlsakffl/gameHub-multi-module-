package com.ams.onionuserapi.controller;

import com.ams.onioncore.dto.ApiResponse;
import com.ams.onioncore.dto.PartyVoiceEventResponse;
import com.ams.onioncore.dto.PartyVoiceJoinRequest;
import com.ams.onioncore.dto.PushNotificationResponse;
import com.ams.onioncore.service.PartyVoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PartyVoiceSocketController {

    private final PartyVoiceService partyVoiceService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/party/{partyId}/voice.join")
    public void join(
            @DestinationVariable Long partyId,
            @Valid @Payload PartyVoiceJoinRequest request,
            Principal principal
    ) {
        PartyVoiceEventResponse event = partyVoiceService.join(principal.getName(), partyId, request.getChannelName());
        broadcastVoiceAndNotifyMembers(event);
    }

    @MessageMapping("/party/{partyId}/voice.leave")
    public void leave(
            @DestinationVariable Long partyId,
            Principal principal
    ) {
        PartyVoiceEventResponse event = partyVoiceService.leave(principal.getName(), partyId);
        broadcastVoiceAndNotifyMembers(event);
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
