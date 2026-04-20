package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.PartyVoicePresence;
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
public class PartyVoicePresenceResponse {
    private Long partyId;
    private String userEmail;
    private String userNickname;
    private String channelName;
    private boolean active;
    private String joinedAt;
    private String updatedAt;

    public static PartyVoicePresenceResponse from(PartyVoicePresence presence) {
        return PartyVoicePresenceResponse.builder()
                .partyId(presence.getParty().getId())
                .userEmail(presence.getUser().getEmail())
                .userNickname(presence.getUser().getNickname())
                .channelName(presence.getChannelName())
                .active(presence.isActive())
                .joinedAt(presence.getJoinedAt() != null ? presence.getJoinedAt().toString() : null)
                .updatedAt(presence.getUpdatedAt() != null ? presence.getUpdatedAt().toString() : null)
                .build();
    }
}
