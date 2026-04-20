package com.ams.onioncore.dto;

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
public class PartyVoiceEventResponse {
    private String eventType;
    private Long partyId;
    private String actorEmail;
    private String actorNickname;
    private String channelName;
    private int activeCount;
    private String occurredAt;
}
