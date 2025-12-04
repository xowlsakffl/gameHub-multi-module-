package com.ams.onioncore.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestRequest {
    private String toNickname;  // 친구 요청 보낼 대상 닉네임
}
