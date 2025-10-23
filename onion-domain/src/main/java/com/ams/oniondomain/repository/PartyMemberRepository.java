package com.ams.oniondomain.repository;

import com.ams.oniondomain.entity.GameParty;
import com.ams.oniondomain.entity.PartyMember;
import com.ams.oniondomain.entity.User;
import com.ams.oniondomain.entity.enums.PartyRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
    boolean existsByPartyAndUser(GameParty party, User user);
    Optional<PartyMember> findByPartyAndUser(GameParty party, User user);
    List<PartyMember> findAllByParty(GameParty party);
}
