package com.ams.onioncore.service;

import com.ams.onioncore.dto.PartyVoiceEventResponse;
import com.ams.onioncore.dto.PartyVoicePresenceResponse;
import com.ams.onioncore.exception.CustomException;
import com.ams.onioncore.exception.ErrorCode;
import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyMember;
import com.ams.oniondomain.entity.PartyVoicePresence;
import com.ams.oniondomain.entity.User;
import com.ams.oniondomain.repository.GamePartyRepository;
import com.ams.oniondomain.repository.PartyMemberRepository;
import com.ams.oniondomain.repository.PartyVoicePresenceRepository;
import com.ams.oniondomain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyVoiceService {

    private final GamePartyRepository gamePartyRepository;
    private final UserRepository userRepository;
    private final PartyMemberRepository partyMemberRepository;
    private final PartyVoicePresenceRepository partyVoicePresenceRepository;

    public PartyVoiceEventResponse join(String email, Long partyId, String channelName) {
        GameParty party = getParty(partyId);
        User user = getUser(email);
        validatePartyMember(party, user);

        String normalizedChannel = channelName.trim();
        String eventType = "VOICE_JOIN";

        PartyVoicePresence presence = partyVoicePresenceRepository.findByPartyAndUser(party, user).orElse(null);
        if (presence == null) {
            presence = partyVoicePresenceRepository.save(
                    PartyVoicePresence.builder()
                            .party(party)
                            .user(user)
                            .channelName(normalizedChannel)
                            .build()
            );
        } else {
            if (presence.isActive() && !presence.getChannelName().equals(normalizedChannel)) {
                eventType = "VOICE_SWITCH";
            }
            presence.joinOrSwitch(normalizedChannel);
        }

        int activeCount = partyVoicePresenceRepository.findAllByPartyAndActiveTrue(party).size();

        return PartyVoiceEventResponse.builder()
                .eventType(eventType)
                .partyId(partyId)
                .actorEmail(user.getEmail())
                .actorNickname(user.getNickname())
                .channelName(normalizedChannel)
                .activeCount(activeCount)
                .occurredAt(LocalDateTime.now().toString())
                .build();
    }

    public PartyVoiceEventResponse leave(String email, Long partyId) {
        GameParty party = getParty(partyId);
        User user = getUser(email);
        validatePartyMember(party, user);

        PartyVoicePresence presence = partyVoicePresenceRepository.findByPartyAndUser(party, user)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        presence.leave();
        int activeCount = partyVoicePresenceRepository.findAllByPartyAndActiveTrue(party).size();

        return PartyVoiceEventResponse.builder()
                .eventType("VOICE_LEAVE")
                .partyId(partyId)
                .actorEmail(user.getEmail())
                .actorNickname(user.getNickname())
                .channelName(presence.getChannelName())
                .activeCount(activeCount)
                .occurredAt(LocalDateTime.now().toString())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PartyVoicePresenceResponse> getActiveUsers(String email, Long partyId, String channelName) {
        GameParty party = getParty(partyId);
        User user = getUser(email);
        validatePartyMember(party, user);

        List<PartyVoicePresence> presences;
        if (channelName == null || channelName.isBlank()) {
            presences = partyVoicePresenceRepository.findAllByPartyAndActiveTrue(party);
        } else {
            presences = partyVoicePresenceRepository.findAllByPartyAndActiveTrueAndChannelName(party, channelName.trim());
        }

        return presences.stream().map(PartyVoicePresenceResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<String> getPartyMemberEmails(Long partyId) {
        GameParty party = getParty(partyId);
        List<PartyMember> members = partyMemberRepository.findAllByParty(party);
        return members.stream().map(member -> member.getUser().getEmail()).toList();
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
