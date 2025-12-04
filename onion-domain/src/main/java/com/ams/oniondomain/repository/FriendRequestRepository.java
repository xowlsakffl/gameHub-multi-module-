package com.ams.oniondomain.repository;

import com.ams.oniondomain.entity.FriendRequest;
import com.ams.oniondomain.entity.User;
import com.ams.oniondomain.entity.enums.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    // 이미 보낸 요청인지 확인
    Optional<FriendRequest> findByFromUserAndToUserAndStatus(
            User from, User to, FriendRequestStatus status
    );

    // 내가 받은 요청 목록
    List<FriendRequest> findByToUserAndStatus(User toUser, FriendRequestStatus status);

    // 내가 보낸 요청 목록
    List<FriendRequest> findByFromUserAndStatus(User fromUser, FriendRequestStatus status);
}
