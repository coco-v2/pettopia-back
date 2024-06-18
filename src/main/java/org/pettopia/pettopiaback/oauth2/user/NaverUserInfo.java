package org.pettopia.pettopiaback.oauth2.user;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private static Map<String, Object> response;

    public NaverUserInfo(Map<String, Object> attributes) {
        response = (Map<String, Object>) attributes.get("response");
    }

    public String getSocialId() {
        return String.valueOf(response.get("id"));
    }

    public String getName() {
        return String.valueOf(response.get("name"));
    }

    @Override
    public String getProvider() {
        return "NAVER";
    }

    public String getEmail() {
        return String.valueOf(response.get("email"));
    }
}
