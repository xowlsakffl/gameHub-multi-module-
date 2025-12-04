package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.Friendship;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendResponse {

    private UserSummaryResponse friend;

    public static FriendResponse from(Friendship fs, String myNickname) {

        boolean isUser1Me = fs.getUser1().getNickname().equals(myNickname);

        return FriendResponse.builder()
                .friend(
                        isUser1Me
                                ? UserSummaryResponse.from(fs.getUser2())
                                : UserSummaryResponse.from(fs.getUser1())
                )
                .build();
    }
}
