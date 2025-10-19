package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.enums.PartyType;
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
    private PartyType type;
    private int maxPlayer;
    private int currentPlayers;
    private String description;
    private String status;
    private String creatorEmail;
    private String createdAt;
    private String updatedAt;

    public static GamePartyResponse from(GameParty party) {
        return GamePartyResponse.builder()
                .id(party.getId())
                .title(party.getTitle())
                .gameName(party.getGameName())
                .type(party.getType())
                .maxPlayer(party.getMaxPlayer())
                .currentPlayers(party.getCurrentPlayers())
                .description(party.getDescription())
                .status(party.getStatus().name())
                .creatorEmail(party.getCreator().getEmail())
                .createdAt(party.getCreatedAt().toString())
                .updatedAt(party.getUpdatedAt() != null ? party.getUpdatedAt().toString() : null)
                .build();
    }
}