package org.pettopia.pettopiaback.jwt.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.domain.RoleType;
import org.pettopia.pettopiaback.domain.Users;
import org.pettopia.pettopiaback.dto.PrincipalDetail;
import org.pettopia.pettopiaback.jwt.exception.CustomExpiredJwtException;
import org.pettopia.pettopiaback.jwt.exception.CustomJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Slf4j
public class JwtUtils {

    public static String secretKey = JwtConstants.key;

    // 헤더에 "Bearer XXX" 형식으로 담겨온 토큰을 추출함
    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    public static String generateToken(Map<String, Object> valueMap, long validTimeMillis) {
        SecretKey key = null;
        try {
            key = Keys.hmacShaKeyFor(JwtUtils.secretKey.getBytes(StandardCharsets.UTF_8));
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validTimeMillis))
                .signWith(key)
                .compact();
    }

    public static Authentication getAuthentication(String token) {
        Map<String, Object> claims = validateToken(token);
        System.out.println(claims);
        String email = (String) claims.get("email");
        String socialId = (String) claims.get("socialId");
        String name = (String) claims.get("name");
        String role = (String) claims.get("role");
        RoleType memberRole = RoleType.valueOf(role);

        Users user = Users.builder()
                .email(email).socialId(socialId).name(name).roleType(memberRole)
                .build();
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRoleType().getValue()));
        PrincipalDetail principalDetail = new PrincipalDetail(user, authorities);

        return new UsernamePasswordAuthenticationToken(principalDetail, "", authorities);
    }

    public static Map<String, Object> validateToken(String token) {
        Map<String, Object> claim = null;
        try {
            SecretKey key = Keys.hmacShaKeyFor(JwtUtils.secretKey.getBytes(StandardCharsets.UTF_8));
            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                    .getBody();
        } catch(ExpiredJwtException expiredJwtException){
            throw new CustomExpiredJwtException("토큰이 만료되었습니다", expiredJwtException);
        } catch(Exception e){
            throw new CustomJwtException("유효하지 않은 토큰입니다", e);
        }
        return claim;
    }

    // 토큰이 만료되었는지 판단함
    public static boolean isExpired(String token) {
        try {
            validateToken(token);
        } catch (Exception e) {
            return (e instanceof CustomExpiredJwtException);
        }
        return false;
    }

    // 토큰의 남은 만료시간 계산함
    public static long tokenRemainTime(Integer expTime) {
        Date expDate = new Date((long) expTime * (1000));
        long remainMs = expDate.getTime() - System.currentTimeMillis();
        return remainMs / (1000 * 60);
    }

    // 만료된 토큰에서 클레임을 가져옴
    public static Map<String, Object> getClaimsWithoutValidation(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException expiredJwtException) {
            log.warn("Expired JWT token. Extracting claims without validation.");
            return expiredJwtException.getClaims();
        }
    }

}
