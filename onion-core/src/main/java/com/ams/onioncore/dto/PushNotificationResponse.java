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
public class PushNotificationResponse {
    private String type;
    private String title;
    private String body;
    private Long partyId;
    private String actorEmail;
    private String actorNickname;
    private String createdAt;
}
