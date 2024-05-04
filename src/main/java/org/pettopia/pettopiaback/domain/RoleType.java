package org.pettopia.pettopiaback.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    GUEST("ROLE_GUEST"), USER("ROLE_USER");

    private String value;
}
