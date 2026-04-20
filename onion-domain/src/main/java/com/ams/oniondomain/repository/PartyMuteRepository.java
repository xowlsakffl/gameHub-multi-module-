package com.ams.oniondomain.repository;

import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyMute;
import com.ams.oniondomain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyMuteRepository extends JpaRepository<PartyMute, Long> {
    Optional<PartyMute> findTopByPartyAndTargetUserAndActiveTrueOrderByCreatedAtDesc(GameParty party, User targetUser);
    List<PartyMute> findAllByPartyAndActiveTrue(GameParty party);
}
