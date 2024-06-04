package org.pettopia.pettopiaback.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.PrincipalDetail;
import org.pettopia.pettopiaback.oauth2.service.OAuth2UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "로그인 컨트롤러", description = "로그인 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final OAuth2UserService oauth2UserService;

    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal PrincipalDetail principal) {
        return oauth2UserService.logout(principal);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/oauth2/authorization/google")
    public ResponseEntity<String> redirectToGoogleLogin() {
        String googleLoginUrl = oauth2UserService.getGoogleLoginUrl();
        return ResponseEntity.ok(googleLoginUrl);
    }


}