package org.pettopia.pettopiaback.jwt.exception;

public class CustomJwtException extends RuntimeException {
    public CustomJwtException(String msg) {
        super(msg);
    }

    public CustomJwtException(String msg, Exception e) {
    }
}