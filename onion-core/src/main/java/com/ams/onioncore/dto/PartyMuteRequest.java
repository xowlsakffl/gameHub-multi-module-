package com.ams.onioncore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class PartyMuteRequest {
    @NotNull(message = "userId is required")
    private Long userId;

    @Min(value = 1, message = "durationMinutes must be at least 1")
    private Integer durationMinutes;

    @Size(max = 255, message = "reason must be at most 255 characters")
    private String reason;
}
