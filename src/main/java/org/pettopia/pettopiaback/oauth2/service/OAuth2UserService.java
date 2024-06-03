package org.pettopia.pettopiaback.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.domain.RoleType;
import org.pettopia.pettopiaback.domain.SocialType;
import org.pettopia.pettopiaback.domain.Users;
import org.pettopia.pettopiaback.dto.PrincipalDetail;
import org.pettopia.pettopiaback.oauth2.user.GoogleUserInfo;
import org.pettopia.pettopiaback.oauth2.user.KakaoUserInfo;
import org.pettopia.pettopiaback.oauth2.user.NaverUserInfo;
import org.pettopia.pettopiaback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.provider.kakao.admin-key}")
    private String kakaoAdminKey;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("--------------------------- OAuth2UserService ---------------------------");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        String accessToken = userRequest.getAccessToken().getTokenValue();
        attributes.put("access_token", accessToken);

        log.info("OAuth2User = {}", oAuth2User);
        log.info("attributes = {}", attributes);

        String providerId = userRequest.getClientRegistration().getRegistrationId();

        if ("kakao".equals(providerId)) {
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
            String socialId = kakaoUserInfo.getSocialId();
            String name = kakaoUserInfo.getName();
            String email = kakaoUserInfo.getEmail();
            return processSocialUser(attributes, socialId, name, email, SocialType.KAKAO, accessToken);
        } else if ("naver".equals(providerId)) {
            NaverUserInfo naverUserInfo = new NaverUserInfo(attributes);
            String socialId = naverUserInfo.getSocialId();
            String name = naverUserInfo.getName();
            String email = naverUserInfo.getEmail();
//            return processSocialUser(attributes, socialId, name, email, SocialType.NAVER, accessToken);
            OAuth2User user = processSocialUser(attributes, socialId, name, email, SocialType.NAVER, accessToken);

            // 네이버 accessToken 저장
            Optional<Users> optionalUser = userRepository.findBySocialId(socialId);
            if (optionalUser.isPresent()) {
                Users savedUser = optionalUser.get();
                savedUser.setAccessToken(accessToken);
                userRepository.save(savedUser);
            }

            return user;

        } else if ("google".equals(providerId)) {
            GoogleUserInfo googleUserInfo = new GoogleUserInfo(attributes);
            String socialId = googleUserInfo.getSocialId();
            String name = googleUserInfo.getName();
            String email = googleUserInfo.getEmail();
            return processSocialUser(attributes, socialId, name, email, SocialType.GOOGLE, accessToken);
        }

        throw new UnsupportedOperationException("Unsupported provider: " + providerId);
    }

    private OAuth2User processSocialUser(Map<String, Object> attributes, String socialId, String name, String email, SocialType type, String accessToken) {
        Optional<Users> bySocialId = userRepository.findBySocialId(socialId);
        Users member = bySocialId.orElseGet(
                () -> saveSocialMember(socialId, name, email, type)
        );

        return new PrincipalDetail(member, Collections.singleton(new SimpleGrantedAuthority(member.getRoleType().getValue())));
    }

    public Users saveSocialMember(String socialId, String name, String email, SocialType type) {
        log.info("--------------------------- saveSocialMember ---------------------------");
        Users newMember = Users.builder()
                .socialId(socialId)
                .name(name)
                .email(email)
                .socialType(type)
                .roleType(RoleType.USER)
                .createAt(LocalDateTime.now())
                .build();
        return userRepository.save(newMember);
    }

    public ResponseEntity<String> logout(PrincipalDetail principal) {
        String socialId = (String) principal.getMemberInfo().get("socialId");

        SocialType provider = userRepository.findSocialTypeBySocialId(socialId);
        String accessToken = userRepository.findSocialAccessTokenBySocialId(socialId);

        log.info("logout - socialId = {}", socialId);
        log.info("logout - provider = {}", provider.name());
        if (provider == SocialType.KAKAO) {
            return kakaoLogout(socialId);
        } else if (provider == SocialType.NAVER) {
            return naverLogout(accessToken);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported provider: " + provider);
    }

    public ResponseEntity<String> kakaoLogout(String userId) {

        String reqUrl = "https://kapi.kakao.com/v1/user/unlink";

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "KakaoAK " + kakaoAdminKey);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String postParams = "target_id_type=user_id&target_id=" + userId;
            conn.getOutputStream().write(postParams.getBytes("UTF-8"));

            int responseCode = conn.getResponseCode();
            log.info("[OAuth2UserService.kakaoLogout] Response Code: {}", responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder responseSb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseSb.append(line);
            }
            String responseBody = responseSb.toString();
            log.info("Kakao logout - Response Body: {}", responseBody);
            return ResponseEntity.ok("Logged out from Kakao successfully");
        } catch (Exception e) {
            log.error("Error occurred while logging out from Kakao: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while logging out from Kakao: " + e.getMessage());
        }
    }

    public ResponseEntity<String> naverLogout(String accessToken) {
        String reqUrl = "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=" + naverClientId + "&client_secret=" + naverClientSecret + "&access_token=" + accessToken;

        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            log.info("[OAuth2UserService.naverLogout] Response Code: {}", responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder responseSb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseSb.append(line);
            }
            String responseBody = responseSb.toString();
            log.info("Naver logout - Response Body: {}", responseBody);
            return ResponseEntity.ok("Logged out from Naver successfully");
        } catch (Exception e) {
            log.error("Error occurred while logging out from Naver: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while logging out from Naver: " + e.getMessage());
        }
    }

//    public void kakaoLogoutVoid(String userId) {
//        String reqUrl = "https://kapi.kakao.com/v1/user/unlink";
//
//        try {
//            URL url = new URL(reqUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Authorization", "KakaoAK " + kakaoAdminKey);
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            conn.setDoOutput(true);
//
//            String postParams = "target_id_type=user_id&target_id=" + userId;
//            conn.getOutputStream().write(postParams.getBytes("UTF-8"));
//
//            int responseCode = conn.getResponseCode();
//            log.info("[OAuth2UserService.kakaoLogout] Response Code: {}", responseCode);
//
//            BufferedReader br;
//            if (responseCode >= 200 && responseCode <= 300) {
//                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//
//            StringBuilder responseSb = new StringBuilder();
//            String line;
//            while ((line = br.readLine()) != null) {
//                responseSb.append(line);
//            }
//            String responseBody = responseSb.toString();
//            log.info("Kakao logout - Response Body: {}", responseBody);
//        } catch (Exception e) {
//            log.error("Error occurred while logging out from Kakao: {}", e.getMessage());
//        }
//    }

}

