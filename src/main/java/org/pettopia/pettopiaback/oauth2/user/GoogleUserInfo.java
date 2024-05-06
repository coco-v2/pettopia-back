package org.pettopia.pettopiaback.oauth2.user;

import java.util.Map;

public class GoogleUserInfo {
    private static Map<String, Object> response;

    public GoogleUserInfo(Map<String, Object> attributes) {
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
