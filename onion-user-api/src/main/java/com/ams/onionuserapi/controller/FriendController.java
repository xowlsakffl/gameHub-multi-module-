package com.ams.onionuserapi.controller;

import com.ams.onioncore.dto.*;
import com.ams.onioncore.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    /** 친구 요청 보내기 */
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<Void>> sendRequest(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestBody FriendRequestRequest request
    ) {
        friendService.sendRequest(email, request);
        return ResponseEntity.ok(ApiResponse.successMessage("친구 요청을 보냈습니다."));
    }

    /** 받은 요청 목록 */
    @GetMapping("/requests/received")
    public ResponseEntity<ApiResponse<List<FriendRequestResponse>>> received(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        List<FriendRequestResponse> list = friendService.getReceivedRequests(email);
        return ResponseEntity.ok(ApiResponse.success(list, "받은 친구 요청 목록 조회 성공"));
    }

    /** 보낸 요청 목록 */
    @GetMapping("/requests/sent")
    public ResponseEntity<ApiResponse<List<FriendRequestResponse>>> sent(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        List<FriendRequestResponse> list = friendService.getSentRequests(email);
        return ResponseEntity.ok(ApiResponse.success(list, "보낸 친구 요청 목록 조회 성공"));
    }

    /** 친구 요청 수락 */
    @PostMapping("/request/{id}/accept")
    public ResponseEntity<ApiResponse<Void>> accept(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long id
    ) {
        friendService.acceptRequest(email, id);
        return ResponseEntity.ok(ApiResponse.successMessage("친구 요청을 수락했습니다."));
    }

    /** 친구 요청 거절 */
    @PostMapping("/request/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long id
    ) {
        friendService.rejectRequest(email, id);
        return ResponseEntity.ok(ApiResponse.successMessage("친구 요청을 거절했습니다."));
    }

    /** 친구 목록 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FriendResponse>>> list(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        List<FriendResponse> list = friendService.getFriends(email);
        return ResponseEntity.ok(ApiResponse.success(list, "친구 목록 조회 성공"));
    }
}
