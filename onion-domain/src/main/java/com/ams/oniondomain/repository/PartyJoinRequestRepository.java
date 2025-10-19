package com.ams.oniondomain.repository;

import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyJoinRequest;
import com.ams.oniondomain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyJoinRequestRepository extends JpaRepository<PartyJoinRequest, Long> {
    Optional<PartyJoinRequest> findByParty(GameParty party);
    Optional<PartyJoinRequest> findByPartyAndRequester(GameParty party, User requester);
    Optional<PartyJoinRequest> findByRequester(User requester);

    List<PartyJoinRequest> findAllByParty(GameParty party);
}
