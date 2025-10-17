package com.ams.onionuserapi.controller;

import com.ams.onioncore.dto.ApiResponse;
import com.ams.onioncore.dto.GamePartyRequest;
import com.ams.onioncore.dto.GamePartyResponse;
import com.ams.onioncore.service.GamePartyService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/party")
@RequiredArgsConstructor
public class GamePartyController {
    private final GamePartyService gamePartyService;
    private final Logger log = LoggerFactory.getLogger(GamePartyController.class);

    /** 모집글 등록 */
    @PostMapping
    public ResponseEntity<ApiResponse<GamePartyResponse>> create(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody GamePartyRequest request
    ) {
        log.info("Authenticated user: {}", user.getUsername());
        GamePartyResponse response = gamePartyService.create(user.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "모집글 등록 성공"));
    }

    /** 전체 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<GamePartyResponse>>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Sort.Direction dir = direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

        Page<GamePartyResponse> response = gamePartyService.getAllPaged(pageable);
        return ResponseEntity.ok(ApiResponse.success(response, "모집글 목록 페이징 조회 성공"));
    }

    /** 단건 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GamePartyResponse>> getById(@PathVariable Long id) {
        GamePartyResponse response = gamePartyService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "모집글 상세 조회 성공"));
    }

    /** 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GamePartyResponse>> update(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id,
            @RequestBody GamePartyRequest request
    ) {
        GamePartyResponse response = gamePartyService.update(user.getUsername(), id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "모집글 수정 성공"));
    }

    /** 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id
    ) {
        gamePartyService.delete(user.getUsername(), id);
        return ResponseEntity.ok(ApiResponse.successMessage("모집글 삭제 성공"));
    }
}
