package com.ams.oniondomain.repository;

import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyChatMessageRepository extends JpaRepository<PartyChatMessage, Long> {
    List<PartyChatMessage> findByPartyOrderByIdDesc(GameParty party, Pageable pageable);
    List<PartyChatMessage> findByPartyAndIdLessThanOrderByIdDesc(GameParty party, Long id, Pageable pageable);
    Optional<PartyChatMessage> findByIdAndParty(Long id, GameParty party);
}
