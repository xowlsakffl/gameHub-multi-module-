package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.PartyChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartyChatMessageResponse {
    private Long id;
    private Long partyId;
    private String senderEmail;
    private String senderNickname;
    private String content;
    private String createdAt;

    public static PartyChatMessageResponse from(PartyChatMessage message) {
        return PartyChatMessageResponse.builder()
                .id(message.getId())
                .partyId(message.getParty().getId())
                .senderEmail(message.getSender().getEmail())
                .senderNickname(message.getSender().getNickname())
                .content(message.getContent())
                .createdAt(message.getCreatedAt().toString())
                .build();
    }
}
