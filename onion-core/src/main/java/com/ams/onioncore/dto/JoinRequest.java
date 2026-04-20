package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.PartyJoinRequest;
import com.ams.oniondomain.entity.enums.JoinRequestStatus;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {
    private Long partyId;
    private String requester;

    @Size(max = 500, message = "message must be at most 500 characters")
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
