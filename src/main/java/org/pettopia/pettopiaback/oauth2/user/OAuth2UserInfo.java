package org.pettopia.pettopiaback.oauth2.user;

public interface OAuth2UserInfo {
    String getProvider();
    String getEmail();
    String getName();
    String getSocialId();
}
