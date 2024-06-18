package org.pettopia.pettopiaback.oauth2.user;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {
    private static Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getSocialId() {
        return String.valueOf(attributes.get("sub"));
    }

    public String getName() {
        return String.valueOf(attributes.get("name"));
    }

    @Override
    public String getProvider() {
        return "GOOGLE";
    }

    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

}
