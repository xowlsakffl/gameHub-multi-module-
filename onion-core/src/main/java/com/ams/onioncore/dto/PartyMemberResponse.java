package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.PartyMember;
import com.ams.oniondomain.entity.enums.PartyRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyMemberResponse {
    private Long userId;
    private String email;
    private String nickname;
    private PartyRole role;
    private String joinedAt;

    public static PartyMemberResponse from(PartyMember member) {
        return PartyMemberResponse.builder()
                .userId(member.getUser().getId())
                .email(member.getUser().getEmail())
                .nickname(member.getUser().getNickname())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt().toString())
                .build();
    }
}