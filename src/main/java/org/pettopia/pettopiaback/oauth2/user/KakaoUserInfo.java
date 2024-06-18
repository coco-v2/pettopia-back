package org.pettopia.pettopiaback.oauth2.user;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    public static String socialId;
    public static Map<String, Object> account;
    public static Map<String, Object> profile;

    public KakaoUserInfo(Map<String, Object> attributes) {
        socialId = String.valueOf(attributes.get("id"));
        account = (Map<String, Object>) attributes.get("kakao_account");
        profile = (Map<String, Object>) account.get("profile");
    }

    public String getSocialId() {
        return socialId;
    }

    public String getName() {
        return String.valueOf(profile.get("nickname"));
    }

    @Override
    public String getProvider() {
        return "KAKAO";
    }

    public String getEmail() {
        return String.valueOf(account.get("email"));
    }

}