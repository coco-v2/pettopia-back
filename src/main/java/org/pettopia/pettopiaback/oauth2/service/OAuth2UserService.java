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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("--------------------------- OAuth2UserService ---------------------------");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        log.info("OAuth2USer = {}", oAuth2User);
        log.info("attributes = {}", attributes);

//        // nameAttributeKey
//        String userNameAttributeName = userRequest.getClientRegistration()
//                .getProviderDetails()
//                .getUserInfoEndpoint()
//                .getUserNameAttributeName();
//        log.info("nameAttributeKey = {}", userNameAttributeName);
//
//        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
//        String socialId = kakaoUserInfo.getSocialId();
//        String name = kakaoUserInfo.getName();
//        String email = kakaoUserInfo.getEmail();
//
//        // 소셜 ID 로 사용자를 조회, 없으면 socialId 와 이름으로 사용자 생성
//        Optional<Users> bySocialId = userRepository.findBySocialId(socialId);
//        Users member = bySocialId.orElseGet(
//                () -> saveSocialMember(socialId, name, email, SocialType.KAKAO)
//        );
//
//        return new PrincipalDetail(member, Collections.singleton(new SimpleGrantedAuthority(member.getRoleType().getValue())),
//                attributes);

        String providerId = userRequest.getClientRegistration().getRegistrationId();

        if ("kakao".equals(providerId)) {
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
            String socialId = kakaoUserInfo.getSocialId();
            String name = kakaoUserInfo.getName();
            String email = kakaoUserInfo.getEmail();
            return processSocialUser(attributes, socialId, name, email, SocialType.KAKAO);
        } else if ("naver".equals(providerId)) {
            NaverUserInfo naverUserInfo = new NaverUserInfo(attributes);
            String socialId = naverUserInfo.getSocialId();
            String name = naverUserInfo.getName();
            String email = naverUserInfo.getEmail();
            return processSocialUser(attributes, socialId, name, email, SocialType.NAVER);
        } else if ("google".equals(providerId)) {
            GoogleUserInfo googleUserInfo = new GoogleUserInfo(attributes);
            String socialId = googleUserInfo.getSocialId();
            String name = googleUserInfo.getName();
            String email = googleUserInfo.getEmail();
            return processSocialUser(attributes, socialId, name, email, SocialType.GOOGLE);
        }

        throw new UnsupportedOperationException("Unsupported provider: " + providerId);

    }

    //만든거

    private OAuth2User processSocialUser(Map<String, Object> attributes, String socialId, String name, String email, SocialType type) {
        Optional<Users> bySocialId = userRepository.findBySocialId(socialId);
        Users member = bySocialId.orElseGet(
                () -> saveSocialMember(socialId, name, email, type)
        );

        return new PrincipalDetail(member, Collections.singleton(new SimpleGrantedAuthority(member.getRoleType().getValue())), attributes);
    }


    // 소셜 ID 로 가입된 사용자가 없으면 새로운 사용자를 만들어 저장한다
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
}