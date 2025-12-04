package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.FriendRequest;
import com.ams.oniondomain.entity.enums.FriendRequestStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestResponse {

    private Long requestId;
    private UserSummaryResponse fromUser;
    private UserSummaryResponse toUser;
    private FriendRequestStatus status;

    public static FriendRequestResponse from(FriendRequest req) {
        return FriendRequestResponse.builder()
                .requestId(req.getId())
                .fromUser(UserSummaryResponse.from(req.getFromUser()))
                .toUser(UserSummaryResponse.from(req.getToUser()))
                .status(req.getStatus())
                .build();
    }
}