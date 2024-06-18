package org.pettopia.pettopiaback.jwt.filter;

import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.jwt.exception.CustomExpiredJwtException;
import org.pettopia.pettopiaback.jwt.exception.CustomJwtException;
import org.pettopia.pettopiaback.jwt.utils.JwtConstants;
import org.pettopia.pettopiaback.jwt.utils.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class JwtVerifyFilter extends OncePerRequestFilter {

    private static final String[] whitelist = {"/signUp", "/login" , "/refresh", "/"
            , "/index.html","/swagger-ui/**", "/v3/api-docs/**","/swagger-ui/index.html","/swagger-ui.html"
            };

    private static final String ACCESS_ENDPOINT = "/api/v1/jwt/access";

    private static void checkAuthorizationHeader(String header) {
        if(header == null) {
            throw new CustomJwtException("토큰이 전달되지 않았습니다");
        } else if (!header.startsWith(JwtConstants.JWT_TYPE)) {
            throw new CustomJwtException("BEARER 로 시작하지 않는 올바르지 않은 토큰 형식입니다");
        }
    }

    // 필터를 거치지 않을 URL 을 설정하고, true 를 return 하면 현재 필터를 건너뛰고 다음 필터로 이동
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {
        log.info("--------------------------- JwtVerifyFilter ---------------------------");

        String authHeader = request.getHeader(JwtConstants.JWT_HEADER);
        String requestURI = request.getRequestURI();

        try {
            checkAuthorizationHeader(authHeader);   // header 가 올바른 형식인지 체크
            String token = JwtUtils.getTokenFromHeader(authHeader);
            log.info("JwtVerifyFilter Token = {}", token);

            if (ACCESS_ENDPOINT.equals(requestURI)) {
                try {
                    // 토큰 검증 (만료된 토큰도 허용)
                    Map<String, Object> claims = JwtUtils.getClaimsWithoutValidation(token);
                    request.setAttribute("claims", claims);
                } catch (CustomJwtException e) {
                    log.error("Invalid JWT token for /access endpoint", e);
                }
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authentication = JwtUtils.getAuthentication(token);
            System.out.println(authentication);
            log.info("JwtVerifyFilter authentication = {}", authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);    // 다음 필터로 이동
        } catch (Exception e) {
            Gson gson = new Gson();
            String json = "";
            if (e instanceof CustomExpiredJwtException) {
                json = gson.toJson(Map.of("Token_Expired", e.getMessage()));
            } else {
                json = gson.toJson(Map.of("error", e.getMessage()));
                System.out.println(json);
            }

            response.setContentType("application/json; charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(json);
            printWriter.close();
        }
    }
}
