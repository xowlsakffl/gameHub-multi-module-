package com.ams.oniondomain.repository;

import com.ams.oniondomain.entity.GameParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePartyRepository extends JpaRepository<GameParty, Long> {
}
