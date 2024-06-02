package org.pettopia.pettopiaback.jwt.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.jwt.exception.CustomJwtException;
import org.pettopia.pettopiaback.jwt.utils.JwtConstants;
import org.pettopia.pettopiaback.jwt.utils.JwtUtils;
import org.pettopia.pettopiaback.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "JWT 컨트롤러")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwt")
public class JwtController {

    private final JwtService jwtService;


    @RequestMapping("/access")
    public ResponseEntity<Map<String, Object>> validateAccess(@RequestParam String authHeader) {
        try {
            log.info("Access Token = {}", authHeader);
            Map<String, Object> response = jwtService.validateAccessToken(authHeader);
            return ResponseEntity.ok(response);
        } catch (CustomJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @RequestMapping("/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, String refreshToken) {
        log.info("Refresh Token = {}", refreshToken);
        if (authHeader == null) {
            throw new CustomJwtException("Access Token 이 존재하지 않습니다");
        } else if (!authHeader.startsWith(JwtConstants.JWT_TYPE)) {
            throw new CustomJwtException("BEARER 로 시작하지 않는 올바르지 않은 토큰 형식입니다");
        }

        String accessToken = JwtUtils.getTokenFromHeader(authHeader);

        // Access Token 의 만료 여부 확인
        if (!JwtUtils.isExpired(accessToken)) {
            return Map.of("Access Token", accessToken, "Refresh Token", refreshToken);
        }

        // refreshToken 검증 후 새로운 토큰 생성 후 전달
        Map<String, Object> claims = JwtUtils.validateToken(refreshToken);
        String newAccessToken = JwtUtils.generateToken(claims, JwtConstants.ACCESS_EXP_TIME);

        String newRefreshToken = refreshToken;
        long expTime = JwtUtils.tokenRemainTime((Integer) claims.get("exp"));   // Refresh Token 남은 만료 시간
        log.info("Refresh Token Remain Expire Time = {}", expTime);
        // Refresh Token 의 만료 시간이 한 시간도 남지 않은 경우
        if (expTime <= 60) {
            newRefreshToken = JwtUtils.generateToken(claims, JwtConstants.REFRESH_EXP_TIME);
        }

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }
}