package org.pettopia.pettopiaback.oauth2.user;

import lombok.AllArgsConstructor;

import java.util.Map;

public class NaverUserInfo {

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

    public String getEmail() {
        return String.valueOf(response.get("email"));
    }
}
