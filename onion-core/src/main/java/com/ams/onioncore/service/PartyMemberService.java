package com.ams.onioncore.service;

import com.ams.onioncore.exception.CustomException;
import com.ams.onioncore.exception.ErrorCode;
import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyMember;
import com.ams.oniondomain.entity.User;
import com.ams.oniondomain.entity.enums.PartyRole;
import com.ams.oniondomain.repository.GamePartyRepository;
import com.ams.oniondomain.repository.PartyMemberRepository;
import com.ams.oniondomain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyMemberService {

    private final GamePartyRepository gamePartyRepository;
    private final PartyMemberRepository memberRepository;
    private final UserRepository userRepository;

    /** 파티 나가기 */
    public void leaveParty(String email, Long partyId) {
        GameParty party = gamePartyRepository.findById(partyId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTY_NOT_FOUND));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        PartyMember member = memberRepository.findByPartyAndUser(party, user)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));

        if (member.getRole() == PartyRole.LEADER) {
            List<PartyMember> others = memberRepository.findAllByParty(party).stream()
                    .filter(m -> !m.getUser().equals(user))
                    .sorted(Comparator.comparing(PartyMember::getJoinedAt))
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

    /** 멤버 강퇴 (방장 전용) */
    public void kickMember(String email, Long partyId, Long memberId) {
        GameParty party = gamePartyRepository.findById(partyId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTY_NOT_FOUND));
        User leader = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        PartyMember leaderMember = memberRepository.findByPartyAndUser(party, leader)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));
        if (leaderMember.getRole() != PartyRole.LEADER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        PartyMember target = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));
        if (target.getRole() == PartyRole.LEADER) {
            throw new CustomException(ErrorCode.CANNOT_KICK_LEADER);
        }

        memberRepository.delete(target);
        party.decrementPlayers();
    }

    /** 방장 위임 */
    public void delegateLeader(String email, Long partyId, Long newLeaderId) {
        GameParty party = gamePartyRepository.findById(partyId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTY_NOT_FOUND));
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
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
}
