package com.ams.onioncore.dto;

import jakarta.validation.constraints.NotBlank;
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
public class PartyVoiceJoinRequest {
    @NotBlank(message = "channelName is required")
    @Size(max = 50, message = "channelName must be at most 50 characters")
    private String channelName;
}
