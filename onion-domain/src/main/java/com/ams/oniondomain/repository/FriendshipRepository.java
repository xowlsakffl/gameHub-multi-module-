package com.ams.oniondomain.repository;

import com.ams.oniondomain.entity.Friendship;
import com.ams.oniondomain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    // A-B 친구 여부 체크
    Optional<Friendship> findByUser1AndUser2(User user1, User user2);

    // 특정 유저 친구 목록
    List<Friendship> findByUser1OrUser2(User user1, User user2);
}
