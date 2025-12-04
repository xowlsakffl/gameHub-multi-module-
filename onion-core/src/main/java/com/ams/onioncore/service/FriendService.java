package com.ams.onioncore.service;

import com.ams.onioncore.dto.FriendRequestRequest;
import com.ams.onioncore.dto.FriendRequestResponse;
import com.ams.onioncore.dto.FriendResponse;
import com.ams.onioncore.dto.UserSummaryResponse;
import com.ams.onioncore.exception.CustomException;
import com.ams.onioncore.exception.ErrorCode;
import com.ams.oniondomain.entity.FriendRequest;
import com.ams.oniondomain.entity.Friendship;
import com.ams.oniondomain.entity.User;
import com.ams.oniondomain.entity.enums.FriendRequestStatus;
import com.ams.oniondomain.repository.FriendRequestRepository;
import com.ams.oniondomain.repository.FriendshipRepository;
import com.ams.oniondomain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;

    /** 친구 요청 보내기 */
    public void sendRequest(String fromNickname, FriendRequestRequest request) {

        User from = userRepository.findByNickname(fromNickname)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User to = userRepository.findByNickname(request.getToNickname())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (from.equals(to)) {
            throw new CustomException(ErrorCode.CANNOT_ADD_SELF);
        }

        if (isFriends(from, to)) {
            throw new CustomException(ErrorCode.ALREADY_FRIENDS);
        }

        friendRequestRepository.findByFromUserAndToUserAndStatus(
                from, to, FriendRequestStatus.PENDING
        ).ifPresent(req -> { throw new CustomException(ErrorCode.ALREADY_REQUESTED); });

        FriendRequest req = FriendRequest.builder()
                .fromUser(from)
                .toUser(to)
                .status(FriendRequestStatus.PENDING)
                .build();

        friendRequestRepository.save(req);
    }

    /** 받은 요청 목록 */
    @Transactional(readOnly = true)
    public List<FriendRequestResponse> getReceivedRequests(String nickname) {

        User me = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return friendRequestRepository.findByToUserAndStatus(me, FriendRequestStatus.PENDING)
                .stream()
                .map(FriendRequestResponse::from)
                .toList();
    }

    /** 보낸 요청 목록 */
    @Transactional(readOnly = true)
    public List<FriendRequestResponse> getSentRequests(String nickname) {

        User me = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return friendRequestRepository.findByFromUserAndStatus(me, FriendRequestStatus.PENDING)
                .stream()
                .map(FriendRequestResponse::from)
                .toList();
    }

    /** 친구 요청 수락 */
    public void acceptRequest(String myNickname, Long requestId) {

        FriendRequest req = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_FOUND));

        if (!req.getToUser().getNickname().equals(myNickname)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        req.accept();

        friendshipRepository.save(Friendship.of(req.getFromUser(), req.getToUser()));
    }

    /** 친구 요청 거절 */
    public void rejectRequest(String myNickname, Long requestId) {

        FriendRequest req = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_FOUND));

        if (!req.getToUser().getNickname().equals(myNickname)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        req.reject();
    }

    /** 친구 목록 조회 */
    @Transactional(readOnly = true)
    public List<FriendResponse> getFriends(String nickname) {

        User me = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Friendship> list = friendshipRepository.findByUser1OrUser2(me, me);

        return list.stream()
                .map(fs -> FriendResponse.from(fs, nickname))
                .toList();
    }

    /** 친구 여부 확인 */
    private boolean isFriends(User a, User b) {

        Long aId = a.getId();
        Long bId = b.getId();

        User user1 = aId < bId ? a : b;
        User user2 = aId < bId ? b : a;

        return friendshipRepository.findByUser1AndUser2(user1, user2).isPresent();
    }
}
