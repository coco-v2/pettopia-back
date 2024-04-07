package org.pettopia.pettopiaback.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "diary_medicine")
public class Users {

    @Id
    @GeneratedValue
    private Long pk;
    @NotNull
    private String name;
    @NotNull
    private String phoneNum;
    @NotNull
    private String email;

    private RoleType roleType;
}
