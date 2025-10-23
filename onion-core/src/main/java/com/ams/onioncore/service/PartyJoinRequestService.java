package com.ams.onioncore.service;

import com.ams.onioncore.dto.GamePartyResponse;
import com.ams.onioncore.dto.JoinRequest;
import com.ams.onioncore.dto.JoinResponse;
import com.ams.onioncore.exception.CustomException;
import com.ams.onioncore.exception.ErrorCode;
import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyJoinRequest;
import com.ams.oniondomain.entity.PartyMember;
import com.ams.oniondomain.entity.User;
import com.ams.oniondomain.entity.enums.JoinRequestStatus;
import com.ams.oniondomain.entity.enums.PartyRole;
import com.ams.oniondomain.entity.enums.PartyStatus;
import com.ams.oniondomain.entity.enums.PartyType;
import com.ams.oniondomain.repository.GamePartyRepository;
import com.ams.oniondomain.repository.PartyJoinRequestRepository;
import com.ams.oniondomain.repository.PartyMemberRepository;
import com.ams.oniondomain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyJoinRequestService {

    private final PartyJoinRequestRepository partyJoinRequestRepository;
    private final GamePartyRepository gamePartyRepository;
    private final UserRepository userRepository;
    private final PartyMemberRepository memberRepository;

    /** 파티 자동 참가 **/
    @Transactional
    public void autoJoin(String email, Long partyId) {
        GameParty party = gamePartyRepository.findById(partyId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTY_NOT_FOUND));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (party.getType() == PartyType.REQUEST_JOIN) {
            throw new CustomException(ErrorCode.INVALID_JOIN_TYPE);
        }

        if (party.getStatus() == PartyStatus.CLOSED) {
            throw new CustomException(ErrorCode.PARTY_CLOSED);
        }

        if (party.getCurrentPlayers() >= party.getMaxPlayer()) {
            throw new CustomException(ErrorCode.PARTY_FULL);
        }

        if (memberRepository.existsByPartyAndUser(party, user)) {
            throw new CustomException(ErrorCode.ALREADY_PARTY_MEMBER);
        }

        PartyMember member = PartyMember.builder()
                .party(party)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();
        memberRepository.save(member);

        party.incrementPlayers();
    }

    /** 파티 참가 요청 (생성) **/
    @Transactional
    public JoinResponse requestJoin(String email, JoinRequest request) {
        GameParty party = gamePartyRepository.findById(request.getPartyId())
                .orElseThrow(() -> new CustomException(ErrorCode.PARTY_NOT_FOUND));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (party.getType() == PartyType.AUTO_JOIN) {
            throw new CustomException(ErrorCode.INVALID_JOIN_TYPE);
        }

        if (party.getStatus() == PartyStatus.CLOSED) {
            throw new CustomException(ErrorCode.PARTY_CLOSED);
        }

        if (party.getCurrentPlayers() >= party.getMaxPlayer()) {
            throw new CustomException(ErrorCode.PARTY_FULL);
        }

        if (memberRepository.existsByPartyAndUser(party, user)) {
            throw new CustomException(ErrorCode.ALREADY_PARTY_MEMBER);
        }

        boolean alreadyPending = partyJoinRequestRepository.findAllByParty(party).stream()
                .anyMatch(r -> r.getRequester().equals(user)
                        && r.getStatus() == JoinRequestStatus.PENDING);
        if (alreadyPending) {
            throw new CustomException(ErrorCode.DUPLICATE_JOIN_REQUEST);
        }

        PartyJoinRequest joinRequest = PartyJoinRequest.builder()
                .party(party)
                .requester(user)
                .message(request.getMessage())
                .status(JoinRequestStatus.PENDING)
                .build();

        return JoinResponse.from(partyJoinRequestRepository.save(joinRequest));
    }

    /** 승인 (방장 전용) **/
    @Transactional
    public void approveRequest(String email, Long requestId) {
        PartyJoinRequest request = partyJoinRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        GameParty party = request.getParty();
        User requester = request.getRequester();

        User approver = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        PartyMember approverMember = memberRepository.findByPartyAndUser(party, approver)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));

        if (approverMember.getRole() != PartyRole.LEADER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 방장만 승인 가능
        }

        if (request.getStatus() == JoinRequestStatus.APPROVED) {
            throw new CustomException(ErrorCode.REQUEST_ALREADY_APPROVED);
        }

        if (memberRepository.existsByPartyAndUser(party, requester)) {
            throw new CustomException(ErrorCode.ALREADY_PARTY_MEMBER);
        }

        if (party.getCurrentPlayers() >= party.getMaxPlayer()) {
            throw new CustomException(ErrorCode.PARTY_FULL);
        }

        partyJoinRequestRepository.findAllByParty(party).stream()
                .filter(r -> r.getRequester().equals(requester))
                .filter(r -> r.getStatus() == JoinRequestStatus.PENDING)
                .forEach(PartyJoinRequest::approve);

        PartyMember member = PartyMember.builder()
                .party(party)
                .user(requester)
                .role(PartyRole.MEMBER)
                .joinedAt(LocalDateTime.now())
                .build();

        memberRepository.save(member);
        party.incrementPlayers();
    }

    /** 거절 (방장 전용) **/
    @Transactional
    public void rejectRequest(String email, Long requestId) {
        PartyJoinRequest request = partyJoinRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        GameParty party = request.getParty();
        User rejector = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        PartyMember leader = memberRepository.findByPartyAndUser(party, rejector)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));

        if (leader.getRole() != PartyRole.LEADER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        if (request.getStatus() == JoinRequestStatus.REJECTED) {
            throw new CustomException(ErrorCode.REQUEST_ALREADY_REJECTED);
        }
        if (request.getStatus() == JoinRequestStatus.APPROVED) {
            throw new CustomException(ErrorCode.REQUEST_ALREADY_APPROVED);
        }

        request.reject();
    }

    /** 방장 전용 - 대기 중 참가 요청 목록 조회 */
    @Transactional(readOnly = true)
    public List<PartyJoinRequest> getPendingRequests(String email, Long partyId) {
        GameParty party = gamePartyRepository.findById(partyId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTY_NOT_FOUND));

        User leader = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        PartyMember member = memberRepository.findByPartyAndUser(party, leader)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTY_MEMBER));

        if (member.getRole() != PartyRole.LEADER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return partyJoinRequestRepository.findAllByParty(party).stream()
                .filter(r -> r.getStatus() == JoinRequestStatus.PENDING)
                .toList();
    }

}
