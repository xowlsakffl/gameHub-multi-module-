package com.ams.onionuserapi.controller;

import com.ams.onioncore.dto.ApiResponse;
import com.ams.onioncore.dto.LoginRequest;
import com.ams.onioncore.dto.SignupRequest;
import com.ams.onioncore.dto.TokenResponse;
import com.ams.onioncore.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok(ApiResponse.successMessage("회원가입 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(token, "로그인 성공"));
    }

    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<?>> checkEmail(@RequestParam String email) {
        boolean available = authService.isEmailAvailable(email);

        return ResponseEntity.ok(
                ApiResponse.success(
                        Map.of("available", available),
                        available ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다."
                )
        );
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse<?>> checkNickname(@RequestParam String nickname) {
        boolean available = authService.isNicknameAvailable(nickname);

        return ResponseEntity.ok(
                ApiResponse.success(
                        Map.of("available", available),
                        available ? "사용 가능한 닉네임입니다." : "이미 사용 중인 닉네임입니다."
                )
        );
    }
}
