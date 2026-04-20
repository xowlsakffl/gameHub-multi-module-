package com.ams.onioncore.service;

import com.ams.onioncore.dto.PartyChatMessageResponse;
import com.ams.onioncore.dto.PartyChatReadResponse;
import com.ams.onioncore.exception.CustomException;
import com.ams.onioncore.exception.ErrorCode;
import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyChatMessage;
import com.ams.oniondomain.entity.PartyChatRead;
import com.ams.oniondomain.entity.User;
import com.ams.oniondomain.repository.GamePartyRepository;
import com.ams.oniondomain.repository.PartyChatMessageRepository;
import com.ams.oniondomain.repository.PartyChatReadRepository;
import com.ams.oniondomain.repository.PartyMemberRepository;
import com.ams.oniondomain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyChatService {

    private final PartyChatMessageRepository partyChatMessageRepository;
    private final PartyChatReadRepository partyChatReadRepository;
    private final GamePartyRepository gamePartyRepository;
    private final UserRepository userRepository;
    private final PartyMemberRepository partyMemberRepository;

    public PartyChatMessageResponse sendMessage(String email, Long partyId, String content) {
        GameParty party = getParty(partyId);
        User user = getUser(email);
        validatePartyMember(party, user);

        PartyChatMessage message = PartyChatMessage.builder()
                .party(party)
                .sender(user)
                .content(content)
                .build();
        PartyChatMessage saved = partyChatMessageRepository.save(message);

        partyChatReadRepository.findByPartyAndUser(party, user)
                .ifPresentOrElse(
                        read -> read.markRead(saved.getId()),
                        () -> partyChatReadRepository.save(
                                PartyChatRead.builder()
                                        .party(party)
                                        .user(user)
                                        .lastReadMessageId(saved.getId())
                                        .build()
                        )
                );

        return PartyChatMessageResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<PartyChatMessageResponse> getMessages(String email, Long partyId, Long beforeMessageId, int limit) {
        GameParty party = getParty(partyId);
        User user = getUser(email);
        validatePartyMember(party, user);

        int safeLimit = Math.min(Math.max(limit, 1), 100);

        List<PartyChatMessage> messages;
        if (beforeMessageId == null) {
            messages = partyChatMessageRepository.findByPartyOrderByIdDesc(party, PageRequest.of(0, safeLimit));
        } else {
            messages = partyChatMessageRepository.findByPartyAndIdLessThanOrderByIdDesc(
                    party, beforeMessageId, PageRequest.of(0, safeLimit)
            );
        }

        List<PartyChatMessage> ordered = new ArrayList<>(messages);
        Collections.reverse(ordered);
        return ordered.stream().map(PartyChatMessageResponse::from).toList();
    }

    public PartyChatReadResponse markRead(String email, Long partyId, Long lastReadMessageId) {
        GameParty party = getParty(partyId);
        User user = getUser(email);
        validatePartyMember(party, user);

        partyChatMessageRepository.findByIdAndParty(lastReadMessageId, party)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        PartyChatRead read = partyChatReadRepository.findByPartyAndUser(party, user)
                .map(existing -> {
                    existing.markRead(lastReadMessageId);
                    return existing;
                })
                .orElseGet(() -> partyChatReadRepository.save(
                        PartyChatRead.builder()
                                .party(party)
                                .user(user)
                                .lastReadMessageId(lastReadMessageId)
                                .build()
                ));

        return PartyChatReadResponse.from(read);
    }

    private GameParty getParty(Long partyId) {
        return gamePartyRepository.findById(partyId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTY_NOT_FOUND));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void validatePartyMember(GameParty party, User user) {
        if (!partyMemberRepository.existsByPartyAndUser(party, user)) {
            throw new CustomException(ErrorCode.NOT_PARTY_MEMBER);
        }
    }
}
