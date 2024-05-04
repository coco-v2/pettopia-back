package org.pettopia.pettopiaback.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserDTO {
    private String email;
    private String password;
    private String name;
}