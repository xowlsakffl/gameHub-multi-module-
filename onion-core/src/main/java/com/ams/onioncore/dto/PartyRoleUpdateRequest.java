package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.enums.PartyRole;
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
public class PartyRoleUpdateRequest {
    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "role is required")
    private PartyRole role;
}
