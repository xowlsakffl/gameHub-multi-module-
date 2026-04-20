package com.ams.oniondomain.repository;

import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyVoicePresence;
import com.ams.oniondomain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyVoicePresenceRepository extends JpaRepository<PartyVoicePresence, Long> {
    Optional<PartyVoicePresence> findByPartyAndUser(GameParty party, User user);
    List<PartyVoicePresence> findAllByPartyAndActiveTrue(GameParty party);
    List<PartyVoicePresence> findAllByPartyAndActiveTrueAndChannelName(GameParty party, String channelName);
}
