package com.ams.onioncore.service;

import com.ams.onioncore.dto.PartyMuteResponse;
import com.ams.onioncore.exception.CustomException;
import com.ams.onioncore.exception.ErrorCode;
import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyMember;
import com.ams.oniondomain.entity.PartyMute;
import com.ams.oniondomain.entity.User;
import com.ams.oniondomain.entity.enums.PartyRole;
import com.ams.oniondomain.repository.GamePartyRepository;
import com.ams.oniondomain.repository.PartyMemberRepository;
import com.ams.oniondomain.repository.PartyMuteRepository;
import com.ams.oniondomain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyMuteService {

    private final GamePartyRepository gamePartyRepository;
    private final UserRepository userRepository;
    private final PartyMemberRepository partyMemberRepository;
    private final PartyMuteRepository partyMuteRepository;

    public PartyMuteResponse mute(String email, Long partyId, Long targetUserId, Integer durationMinutes, String reason) {
        GameParty party = getParty(partyId);
        User actor = getUser(email);
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        PartyMember actorMember = partyMemberRepository.findByPartyAndUser(party, actor)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));
        PartyMember targetMember = partyMemberRepository.findByPartyAndUser(party, target)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));

        validateMutePermission(actorMember, targetMember);

        partyMuteRepository.findTopByPartyAndTargetUserAndActiveTrueOrderByCreatedAtDesc(party, target)
                .ifPresent(existing -> {
                    if (existing.isCurrentlyActive()) {
                        throw new CustomException(ErrorCode.BAD_REQUEST);
                    }
                    existing.unmute();
                });

        LocalDateTime expiresAt = durationMinutes == null ? null : LocalDateTime.now().plusMinutes(durationMinutes);
        PartyMute mute = partyMuteRepository.save(
                PartyMute.builder()
                        .party(party)
                        .targetUser(target)
                        .mutedBy(actor)
                        .reason(reason)
                        .expiresAt(expiresAt)
                        .build()
        );

        return PartyMuteResponse.from(mute);
    }

    public void unmute(String email, Long partyId, Long targetUserId) {
        GameParty party = getParty(partyId);
        User actor = getUser(email);
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        PartyMember actorMember = partyMemberRepository.findByPartyAndUser(party, actor)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));
        PartyMember targetMember = partyMemberRepository.findByPartyAndUser(party, target)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));

        validateMutePermission(actorMember, targetMember);

        PartyMute mute = partyMuteRepository.findTopByPartyAndTargetUserAndActiveTrueOrderByCreatedAtDesc(party, target)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
        mute.unmute();
    }

    @Transactional(readOnly = true)
    public List<PartyMuteResponse> getActiveMutes(String email, Long partyId) {
        GameParty party = getParty(partyId);
        User actor = getUser(email);
        PartyMember actorMember = partyMemberRepository.findByPartyAndUser(party, actor)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));

        if (actorMember.getRole() == PartyRole.MEMBER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return partyMuteRepository.findAllByPartyAndActiveTrue(party).stream()
                .filter(PartyMute::isCurrentlyActive)
                .map(PartyMuteResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean isMuted(GameParty party, User user) {
        return partyMuteRepository.findTopByPartyAndTargetUserAndActiveTrueOrderByCreatedAtDesc(party, user)
                .map(PartyMute::isCurrentlyActive)
                .orElse(false);
    }

    public void validateNotMuted(GameParty party, User user) {
        if (isMuted(party, user)) {
            throw new CustomException(ErrorCode.USER_MUTED);
        }
    }

    private void validateMutePermission(PartyMember actor, PartyMember target) {
        if (actor.getRole() == PartyRole.MEMBER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        if (target.getRole() == PartyRole.LEADER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        if (actor.getRole() == PartyRole.MANAGER && target.getRole() != PartyRole.MEMBER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    private GameParty getParty(Long partyId) {
        return gamePartyRepository.findById(partyId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTY_NOT_FOUND));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
