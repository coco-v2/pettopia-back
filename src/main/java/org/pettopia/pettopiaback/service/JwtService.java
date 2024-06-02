package org.pettopia.pettopiaback.service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.jwt.exception.CustomExpiredJwtException;
import org.pettopia.pettopiaback.jwt.exception.CustomJwtException;
import org.pettopia.pettopiaback.jwt.utils.JwtConstants;
import org.pettopia.pettopiaback.jwt.utils.JwtUtils;
import org.pettopia.pettopiaback.repository.RedisRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final RedisRepository redisRepository;

    public Map<String, Object> validateAccessToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(JwtConstants.JWT_TYPE)) {
            throw new CustomJwtException("유효하지 않은 토큰 형식입니다.");
        }

        String accessToken = JwtUtils.getTokenFromHeader(authHeader);

        try {
            // Access Token 유효성 검사
            JwtUtils.validateToken(accessToken);
            return Map.of("message", "Access Token이 유효합니다.");
        } catch (ExpiredJwtException e) {
            // Access Token이 만료된 경우 로직 처리
            return handleExpiredAccessToken(accessToken);
        } catch (Exception e) {
            // 기타 예외 처리
            throw new CustomJwtException("Access Token 검증 중 에러가 발생했습니다.");
        }
    }

    private Map<String, Object> handleExpiredAccessToken(String accessToken) {
        // Access Token에서 Claims 추출 (검증 없이)
        Map<String, Object> claims = JwtUtils.getClaimsWithoutValidation(accessToken);
        String userId = (String) claims.get("socialId");
        System.out.println("userId========"+userId);

        // Redis에서 Refresh Token 가져오기
        String refreshToken = redisRepository.getRefreshToken(userId);
        if (refreshToken == null) {
            throw new CustomJwtException("Refresh Token이 존재하지 않습니다.");
        }
        log.info("refreshtoken ???????? {}", refreshToken);

        try {
            Map<String, Object> refreshTokenClaims = JwtUtils.validateToken(refreshToken);
            log.info("refreshToken57 = {}", refreshTokenClaims);
            String newAccessToken = JwtUtils.generateToken(claims, JwtConstants.ACCESS_EXP_TIME);
            log.info("newAccessToken? {}", newAccessToken);
            long expTime = JwtUtils.tokenRemainTime((Integer) refreshTokenClaims.get("exp"));
            log.info("expTime ? {}" , expTime);
            if (expTime <= 60) {
                String newRefreshToken = JwtUtils.generateToken(refreshTokenClaims, JwtConstants.REFRESH_EXP_TIME);
                redisRepository.saveRefreshToken(userId, newRefreshToken);
                log.info("new token generated.");

                return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
            }
            log.info("new token generated.");

            return Map.of("accessToken", newAccessToken, "refreshToken", refreshToken);
        } catch (CustomExpiredJwtException e) {
            throw new CustomJwtException("Refresh Token이 만료되었습니다.", e);
        } catch (Exception e) {
            throw new CustomJwtException("Refresh Token 검증 중 에러가 발생했습니다.", e);
        }
    }

}

