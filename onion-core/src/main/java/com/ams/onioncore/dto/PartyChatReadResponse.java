package com.ams.onioncore.dto;

import com.ams.oniondomain.entity.PartyChatRead;
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
public class PartyChatReadResponse {
    private Long partyId;
    private String readerEmail;
    private Long lastReadMessageId;
    private String readAt;

    public static PartyChatReadResponse from(PartyChatRead read) {
        return PartyChatReadResponse.builder()
                .partyId(read.getParty().getId())
                .readerEmail(read.getUser().getEmail())
                .lastReadMessageId(read.getLastReadMessageId())
                .readAt(read.getReadAt().toString())
                .build();
    }
}
