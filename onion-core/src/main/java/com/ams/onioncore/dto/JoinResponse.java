package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.PartyJoinRequest;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinResponse {
    private Long id;
    private Long partyId;
    private String partyTitle;
    private String requesterEmail;
    private String message;
    private String status;
    private String createdAt;

    public static JoinResponse from(PartyJoinRequest partyJoinRequest) {
        return JoinResponse.builder()
                .id(partyJoinRequest.getId())
                .partyId(partyJoinRequest.getParty().getId())
                .partyTitle(partyJoinRequest.getParty().getTitle())
                .requesterEmail(partyJoinRequest.getRequester().getEmail())
                .message(partyJoinRequest.getMessage())
                .status(partyJoinRequest.getStatus().name())
                .createdAt(partyJoinRequest.getCreatedAt().toString())
                .build();
    }
}
