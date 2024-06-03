package org.pettopia.pettopiaback.security.handler;

import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.PrincipalDetail;
import org.pettopia.pettopiaback.jwt.utils.JwtConstants;
import org.pettopia.pettopiaback.jwt.utils.JwtUtils;
import org.pettopia.pettopiaback.repository.RedisRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CommonLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final RedisRepository redisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request
            , HttpServletResponse response, Authentication authentication
    ) throws IOException, ServletException {
        log.info("--------------------------- CommonLoginSuccessHandler ---------------------------");

        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();

        log.info("authentication.getPrincipal() = {}", principal);

        Map<String, Object> responseMap = principal.getMemberInfo();
        responseMap.put("accessToken", JwtUtils.generateToken(responseMap, JwtConstants.ACCESS_EXP_TIME));
        responseMap.put("refreshToken", JwtUtils.generateToken(responseMap, JwtConstants.REFRESH_EXP_TIME));

        String refreshToken = JwtUtils.generateToken(responseMap, JwtConstants.REFRESH_EXP_TIME);
        responseMap.put("refreshToken", refreshToken);

        // Redis에 refreshToken 저장
        String userId = (String) responseMap.get("socialId");
        redisRepository.saveRefreshToken(userId, refreshToken);


        log.info("==============================success==================================");

        Gson gson = new Gson();
        String json = gson.toJson(responseMap);

        response.setContentType("application/json; charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println(json);
        writer.flush();
    }
}