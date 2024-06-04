package org.pettopia.pettopiaback.oauth2.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.pettopia.pettopiaback.oauth2.service.OAuth2UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OAuth2Controller {

    private final OAuth2UserService oAuth2UserService;
    @CrossOrigin(origins = "*")
    @GetMapping("/oauth2/authorization/google")
    public ResponseEntity<String> redirectToGoogleLogin() {
        String googleLoginUrl = oAuth2UserService.getGoogleLoginUrl();
        return ResponseEntity.ok(googleLoginUrl);
    }
//    public void redirectToGoogleLogin(HttpServletResponse response) throws IOException {
//        String googleLoginUrl = oAuth2UserService.getGoogleLoginUrl();
//        response.sendRedirect(googleLoginUrl);
//    }


}
