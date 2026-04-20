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
public class PartyChatSendRequest {
    @NotBlank(message = "content is required")
    @Size(max = 1000, message = "content must be at most 1000 characters")
    private String content;
}
