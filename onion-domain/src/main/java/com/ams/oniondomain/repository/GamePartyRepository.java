package com.ams.oniondomain.repository;

import com.ams.oniondomain.entity.GameParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GamePartyRepository extends JpaRepository<GameParty, Long> {
    Optional<GameParty> findByInviteCode(String inviteCode);
    boolean existsByInviteCode(String inviteCode);
}
