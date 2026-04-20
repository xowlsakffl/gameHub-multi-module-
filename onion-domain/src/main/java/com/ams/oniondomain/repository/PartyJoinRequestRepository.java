package com.ams.oniondomain.repository;

import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyJoinRequest;
import com.ams.oniondomain.entity.User;
import com.ams.oniondomain.entity.enums.JoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyJoinRequestRepository extends JpaRepository<PartyJoinRequest, Long> {
    Optional<PartyJoinRequest> findByParty(GameParty party);
    Optional<PartyJoinRequest> findByPartyAndRequester(GameParty party, User requester);
    Optional<PartyJoinRequest> findByRequester(User requester);
    boolean existsByPartyAndRequesterAndStatus(GameParty party, User requester, JoinRequestStatus status);

    List<PartyJoinRequest> findAllByParty(GameParty party);
}
