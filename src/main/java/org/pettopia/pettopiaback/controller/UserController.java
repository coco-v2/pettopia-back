package org.pettopia.pettopiaback.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.PrincipalDetail;
import org.pettopia.pettopiaback.oauth2.service.OAuth2UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "로그인 컨트롤러", description = "로그인 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final OAuth2UserService oauth2UserService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal PrincipalDetail principal) {
        return oauth2UserService.logout(principal);
    }


//    @PostMapping("/logout/kakao")
//    public ResponseEntity<String> logoutKakao(@AuthenticationPrincipal PrincipalDetail principal) {
//        String userId = (String) principal.getMemberInfo().get("socialId");
//        log.info("userId = {}", userId);
//        if (userId == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID not found");
//        }
//
//        oauth2UserService.kakaoLogoutVoid(userId);
//        return ResponseEntity.ok("Logged out from Kakao successfully");
//    }




}