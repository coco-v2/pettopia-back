package org.pettopia.pettopiaback.jwt.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConstants {

    public static String key;

    public static int ACCESS_EXP_TIME;

    public static int REFRESH_EXP_TIME;

    public static String JWT_HEADER;

    public static String JWT_TYPE;

    @Value("${jwt.key}")
    public void setKey(String key) {
        JwtConstants.key = key;
    }

    @Value("${jwt.access-exp-time}")
    public void setAccessExpTime(int accessExpTime) {
        JwtConstants.ACCESS_EXP_TIME = accessExpTime;
    }

    @Value("${jwt.refresh-exp-time}")
    public void setRefreshExpTime(int refreshExpTime) {
        JwtConstants.REFRESH_EXP_TIME = refreshExpTime;
    }

    @Value("${jwt-header}")
    public void setJwtHeader(String jwtHeader) {
        JwtConstants.JWT_HEADER = jwtHeader;
    }

    @Value("${jwt-type}")
    public void setJwtType(String jwtType) {
        JwtConstants.JWT_TYPE = jwtType;
    }
}
