package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.enums.PartyType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamePartyRequest {
    @NotBlank(message = "title is required")
    @Size(max = 100, message = "title must be at most 100 characters")
    private String title;

    @NotBlank(message = "gameName is required")
    @Size(max = 50, message = "gameName must be at most 50 characters")
    private String gameName;

    @Min(value = 2, message = "maxPlayer must be at least 2")
    @Max(value = 100, message = "maxPlayer must be at most 100")
    private int maxPlayer;

    @Size(max = 1000, message = "description must be at most 1000 characters")
    private String description;

    @NotNull(message = "type is required")
    private PartyType type;
}
