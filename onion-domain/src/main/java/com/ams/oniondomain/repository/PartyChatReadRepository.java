package com.ams.oniondomain.repository;

import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyChatRead;
import com.ams.oniondomain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyChatReadRepository extends JpaRepository<PartyChatRead, Long> {
    Optional<PartyChatRead> findByPartyAndUser(GameParty party, User user);
    List<PartyChatRead> findAllByParty(GameParty party);
}
