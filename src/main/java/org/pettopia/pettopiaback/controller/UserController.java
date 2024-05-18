package org.pettopia.pettopiaback.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.domain.Users;
import org.pettopia.pettopiaback.service.UserService;
import org.pettopia.pettopiaback.dto.UserDTO;
import org.pettopia.pettopiaback.oauth2.service.OAuth2UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Tag(name = "로그인", description = "로그인 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final OAuth2UserService oauth2UserService;



    @PostMapping("/signUp")
    public Map<String, String> signUp(@RequestBody UserDTO userDTO) {
        log.info("--------------------------- UserController ---------------------------");
        log.info("userDTO = {}", userDTO);
        Map<String, String> response = new HashMap<>();
        Optional<Users> byEmail = userService.findByEmail(userDTO.getEmail());
        if (byEmail.isPresent()) {
            response.put("error", "이미 존재하는 이메일입니다");
        } else {
            userService.saveMember(userDTO);
            response.put("success", "성공적으로 처리하였습니다");
        }
        return response;
    }

    @PostMapping("/logout/kakao")
    public ResponseEntity<String> logoutKakao(@AuthenticationPrincipal OAuth2User principal) {
        String accessToken = (String) principal.getAttribute("access_token");
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Access token not found");
        }

        oauth2UserService.kakaoLogout(accessToken);
        return ResponseEntity.ok("Logged out from Kakao successfully");
    }


}