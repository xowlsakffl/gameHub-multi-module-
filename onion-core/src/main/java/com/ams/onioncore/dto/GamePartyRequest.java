package com.ams.onioncore.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamePartyRequest {
    private String title;
    private String gameName;
    private int maxPlayer;
    private String description;
}
