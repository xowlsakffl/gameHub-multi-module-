package com.ams.onioncore.dto;

import jakarta.validation.constraints.NotNull;
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
public class PartyChatReadRequest {
    @NotNull(message = "lastReadMessageId is required")
    private Long lastReadMessageId;
}
