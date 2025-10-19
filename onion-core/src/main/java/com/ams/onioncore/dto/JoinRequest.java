package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.PartyJoinRequest;
import com.ams.oniondomain.entity.enums.JoinRequestStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {
    private Long partyId;
    private String requester;
    private String message;
    private JoinRequestStatus status;

    public static JoinRequest from(PartyJoinRequest entity) {
        return JoinRequest.builder()
                .partyId(entity.getParty().getId())
                .requester(entity.getRequester().getEmail())
                .message(entity.getMessage())
                .status(entity.getStatus())
                .build();
    }
}
