package com.ams.onioncore.service;

import com.ams.onioncore.dto.PartyInviteCodeResponse;
import com.ams.onioncore.exception.CustomException;
import com.ams.onioncore.exception.ErrorCode;
import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyMember;
import com.ams.oniondomain.entity.User;
import com.ams.oniondomain.entity.enums.PartyRole;
import com.ams.oniondomain.entity.enums.PartyStatus;
import com.ams.oniondomain.repository.GamePartyRepository;
import com.ams.oniondomain.repository.PartyMemberRepository;
import com.ams.oniondomain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyMemberService {

    private final GamePartyRepository gamePartyRepository;
    private final PartyMemberRepository memberRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<PartyMember> getMembersByParty(Long partyId) {
        GameParty party = gamePartyRepository.findById(partyId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTY_NOT_FOUND));
        return memberRepository.findAllByParty(party);
    }

    @Transactional(readOnly = true)
    public List<GameParty> getMyParties(String email) {
        User user = getUser(email);
        List<PartyMember> myMemberships = memberRepository.findAllByUser(user);
        return myMemberships.stream().map(PartyMember::getParty).toList();
    }

    public void leaveParty(String email, Long partyId) {
        GameParty party = getParty(partyId);
        User user = getUser(email);

        PartyMember member = memberRepository.findByPartyAndUser(party, user)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));

        if (member.getRole() == PartyRole.LEADER) {
            List<PartyMember> others = memberRepository.findAllByParty(party).stream()
                    .filter(m -> !m.getUser().equals(user))
                    .sorted(Comparator
                            .comparing((PartyMember m) -> m.getRole() == PartyRole.MANAGER ? 0 : 1)
                            .thenComparing(PartyMember::getJoinedAt))
                    .toList();

            if (others.isEmpty()) {
                memberRepository.delete(member);
                party.decrementPlayers();
                return;
            }

            PartyMember newLeader = others.get(0);
            newLeader.changeRole(PartyRole.LEADER);

            memberRepository.delete(member);
            party.decrementPlayers();
            return;
        }

        memberRepository.delete(member);
        party.decrementPlayers();
    }

    public void kickMember(String email, Long partyId, Long memberId) {
        GameParty party = getParty(partyId);
        User actor = getUser(email);

        PartyMember actorMember = memberRepository.findByPartyAndUser(party, actor)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));

        if (actorMember.getRole() == PartyRole.MEMBER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        PartyMember target = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));

        if (!target.getParty().equals(party)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        if (target.getUser().equals(party.getCreator())) {
            throw new CustomException(ErrorCode.CANNOT_KICK_CREATOR);
        }

        if (target.getRole() == PartyRole.LEADER) {
            throw new CustomException(ErrorCode.CANNOT_KICK_LEADER);
        }

        if (actorMember.getRole() == PartyRole.MANAGER && target.getRole() != PartyRole.MEMBER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        memberRepository.delete(target);
        party.decrementPlayers();
    }

    public void delegateLeader(String email, Long partyId, Long newLeaderId) {
        GameParty party = getParty(partyId);
        User currentUser = getUser(email);
        User newLeader = userRepository.findById(newLeaderId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        PartyMember currentLeader = memberRepository.findByPartyAndUser(party, currentUser)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));
        if (currentLeader.getRole() != PartyRole.LEADER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        PartyMember targetMember = memberRepository.findByPartyAndUser(party, newLeader)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));

        currentLeader.changeRole(PartyRole.MEMBER);
        targetMember.changeRole(PartyRole.LEADER);
    }

    public void updateRole(String email, Long partyId, Long targetUserId, PartyRole role) {
        if (role == PartyRole.LEADER) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        GameParty party = getParty(partyId);
        User actor = getUser(email);
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        PartyMember actorMember = memberRepository.findByPartyAndUser(party, actor)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));
        if (actorMember.getRole() != PartyRole.LEADER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        PartyMember target = memberRepository.findByPartyAndUser(party, targetUser)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));

        if (target.getRole() == PartyRole.LEADER) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        target.changeRole(role);
    }

    @Transactional(readOnly = true)
    public PartyInviteCodeResponse getInviteCode(String email, Long partyId) {
        GameParty party = getParty(partyId);
        User user = getUser(email);
        ensureMember(party, user);

        return PartyInviteCodeResponse.builder()
                .partyId(partyId)
                .inviteCode(party.getInviteCode())
                .build();
    }

    public PartyInviteCodeResponse regenerateInviteCode(String email, Long partyId) {
        GameParty party = getParty(partyId);
        User actor = getUser(email);

        PartyMember actorMember = memberRepository.findByPartyAndUser(party, actor)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));
        if (actorMember.getRole() != PartyRole.LEADER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        do {
            party.regenerateInviteCode();
        } while (gamePartyRepository.existsByInviteCode(party.getInviteCode()));

        return PartyInviteCodeResponse.builder()
                .partyId(partyId)
                .inviteCode(party.getInviteCode())
                .build();
    }

    public void joinByInviteCode(String email, String inviteCode) {
        GameParty party = gamePartyRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTY_NOT_FOUND));
        User user = getUser(email);

        if (party.getStatus() == PartyStatus.CLOSED) {
            throw new CustomException(ErrorCode.PARTY_CLOSED);
        }

        if (party.getCurrentPlayers() >= party.getMaxPlayer()) {
            throw new CustomException(ErrorCode.PARTY_FULL);
        }

        if (memberRepository.existsByPartyAndUser(party, user)) {
            throw new CustomException(ErrorCode.ALREADY_PARTY_MEMBER);
        }

        memberRepository.save(PartyMember.builder()
                .party(party)
                .user(user)
                .role(PartyRole.MEMBER)
                .joinedAt(LocalDateTime.now())
                .build());

        party.incrementPlayers();
    }

    private GameParty getParty(Long partyId) {
        return gamePartyRepository.findById(partyId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTY_NOT_FOUND));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void ensureMember(GameParty party, User user) {
        if (!memberRepository.existsByPartyAndUser(party, user)) {
            throw new CustomException(ErrorCode.NOT_PARTY_MEMBER);
        }
    }
}
