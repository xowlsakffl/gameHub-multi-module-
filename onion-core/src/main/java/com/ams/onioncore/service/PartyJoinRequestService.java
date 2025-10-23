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

        if (memberRepository.existsByPartyAndUser(party, user)) {
            throw new CustomException(ErrorCode.ALREADY_PARTY_MEMBER);
        }

        PartyJoinRequest joinRequest = PartyJoinRequest.builder()
                .party(party)
                .requester(user)
                .message(request.getMessage())
                .status(JoinRequestStatus.PENDING)
                .build();

        return JoinResponse.from(partyJoinRequestRepository.save(joinRequest));
    }

    /** 승인 **/
    @Transactional
    public void approveRequest(Long requestId) {
        PartyJoinRequest request = partyJoinRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        if (request.getStatus() == JoinRequestStatus.APPROVED) {
            throw new CustomException(ErrorCode.REQUEST_ALREADY_APPROVED);
        }

        GameParty party = request.getParty();
        User requester = request.getRequester();

        if (memberRepository.existsByPartyAndUser(party, requester)) {
            throw new CustomException(ErrorCode.ALREADY_PARTY_MEMBER);
        }

        List<PartyJoinRequest> pendingRequests = partyJoinRequestRepository.findAllByParty(party).stream()
                .filter(r -> r.getRequester().equals(requester))
                .filter(r -> r.getStatus() == JoinRequestStatus.PENDING)
                .toList();

        if (pendingRequests.isEmpty()) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        pendingRequests.forEach(PartyJoinRequest::approve);

        PartyMember member = PartyMember.builder()
                .party(party)
                .user(requester)
                .joinedAt(LocalDateTime.now())
                .build();
        memberRepository.save(member);

        party.incrementPlayers();
    }

    /** 거절 **/
    public void rejectRequest(Long requestId) {
        PartyJoinRequest request = partyJoinRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        if (request.getStatus() == JoinRequestStatus.REJECTED) {
            throw new CustomException(ErrorCode.REQUEST_ALREADY_REJECTED);
        }

        request.reject();
    }
}
