package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.GameParty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamePartyResponse {
    private Long id;
    private String title;
    private String gameName;
    private int maxPlayer;
    private int currentPlayers;
    private String description;
    private String status;
    private String creatorEmail;
    private String createdAt;

    public static GamePartyResponse from(GameParty party) {
        return GamePartyResponse.builder()
                .id(party.getId())
                .title(party.getTitle())
                .gameName(party.getGameName())
                .maxPlayer(party.getMaxPlayer())
                .currentPlayers(party.getCurrentPlayers())
                .description(party.getDescription())
                .status(party.getStatus().name())
                .creatorEmail(party.getCreator().getEmail())
                .createdAt(party.getCreatedAt().toString())
                .build();
    }
}