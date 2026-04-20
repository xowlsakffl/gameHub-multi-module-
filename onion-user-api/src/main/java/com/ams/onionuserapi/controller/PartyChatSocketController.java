package com.ams.onionuserapi.controller;

import com.ams.onioncore.dto.ApiResponse;
import com.ams.onioncore.dto.PartyChatMessageResponse;
import com.ams.onioncore.dto.PartyChatReadRequest;
import com.ams.onioncore.dto.PartyChatReadResponse;
import com.ams.onioncore.dto.PartyChatSendRequest;
import com.ams.onioncore.service.PartyChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PartyChatSocketController {

    private final PartyChatService partyChatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/party/{partyId}/chat.send")
    public void sendMessage(
            @DestinationVariable Long partyId,
            @Valid @Payload PartyChatSendRequest request,
            Principal principal
    ) {
        PartyChatMessageResponse response =
                partyChatService.sendMessage(principal.getName(), partyId, request.getContent());

        messagingTemplate.convertAndSend(
                "/topic/party." + partyId + ".chat",
                ApiResponse.success(response, "메시지 전송 성공")
        );
    }

    @MessageMapping("/party/{partyId}/chat.read")
    public void markRead(
            @DestinationVariable Long partyId,
            @Valid @Payload PartyChatReadRequest request,
            Principal principal
    ) {
        PartyChatReadResponse response =
                partyChatService.markRead(principal.getName(), partyId, request.getLastReadMessageId());

        messagingTemplate.convertAndSend(
                "/topic/party." + partyId + ".read",
                ApiResponse.success(response, "읽음 상태 갱신")
        );
    }
}
