package org.pettopia.pettopiaback.service;

import lombok.RequiredArgsConstructor;
import org.pettopia.pettopiaback.jwt.exception.CustomExpiredJwtException;
import org.pettopia.pettopiaback.jwt.exception.CustomJwtException;
import org.pettopia.pettopiaback.jwt.utils.JwtConstants;
import org.pettopia.pettopiaback.jwt.utils.JwtUtils;
import org.pettopia.pettopiaback.repository.RedisRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RedisRepository redisRepository;

    public Map<String, Object> validateAccessToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(JwtConstants.JWT_TYPE)) {
            throw new CustomJwtException("유효하지 않은 토큰 형식입니다");
        }

        String accessToken = JwtUtils.getTokenFromHeader(authHeader);

        try {
            // Access Token이 유효한 경우
            JwtUtils.validateToken(accessToken);
            throw new CustomJwtException("Access Token이 만료되지 않았습니다");
        } catch (CustomExpiredJwtException e) {
            // Access Token이 만료된 경우
            Map<String, Object> claims = JwtUtils.getClaimsWithoutValidation(accessToken);
            String userId = (String) claims.get("socialId");

            // Redis에서 Refresh Token 가져오기
            String refreshToken = redisRepository.getRefreshToken(userId);
            System.out.println("지금 refreshtoken:-------------------"+refreshToken);
            if (refreshToken == null) {
                throw new CustomJwtException("Refresh Token이 존재하지 않습니다");
            }

            // Refresh Token 검증
            Map<String, Object> refreshTokenClaims = JwtUtils.validateToken(refreshToken);

            // 새로운 Access Token 생성
            String newAccessToken = JwtUtils.generateToken(refreshTokenClaims, JwtConstants.ACCESS_EXP_TIME);

            // Refresh Token의 남은 만료시간 확인 후 갱신
            String newRefreshToken = refreshToken;
            long expTime = JwtUtils.tokenRemainTime((Integer) refreshTokenClaims.get("exp"));
            if (expTime <= 60) {
                newRefreshToken = JwtUtils.generateToken(refreshTokenClaims, JwtConstants.REFRESH_EXP_TIME);
                redisRepository.saveRefreshToken(userId, newRefreshToken);
            }

            return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
        }
    }
}
