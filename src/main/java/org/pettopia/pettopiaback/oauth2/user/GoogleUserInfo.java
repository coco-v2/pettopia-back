package org.pettopia.pettopiaback.oauth2.user;

import java.util.Map;

public class GoogleUserInfo {
    private static Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;

//        response = (Map<String, Object>) attributes.get("response");
    }


    public String getSocialId() {
        return String.valueOf(attributes.get("sub"));
    }

    public String getName() {
        return String.valueOf(attributes.get("name"));
    }

    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

}
