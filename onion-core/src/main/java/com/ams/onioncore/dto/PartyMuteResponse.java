package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.PartyMute;
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
public class PartyMuteResponse {
    private Long id;
    private Long partyId;
    private Long targetUserId;
    private String targetEmail;
    private String targetNickname;
    private String reason;
    private String expiresAt;
    private boolean active;
    private String mutedByEmail;

    public static PartyMuteResponse from(PartyMute mute) {
        return PartyMuteResponse.builder()
                .id(mute.getId())
                .partyId(mute.getParty().getId())
                .targetUserId(mute.getTargetUser().getId())
                .targetEmail(mute.getTargetUser().getEmail())
                .targetNickname(mute.getTargetUser().getNickname())
                .reason(mute.getReason())
                .expiresAt(mute.getExpiresAt() != null ? mute.getExpiresAt().toString() : null)
                .active(mute.isCurrentlyActive())
                .mutedByEmail(mute.getMutedBy().getEmail())
                .build();
    }
}
