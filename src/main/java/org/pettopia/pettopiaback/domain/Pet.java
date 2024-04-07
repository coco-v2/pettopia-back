package org.pettopia.pettopiaback.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue
    private Long pk;

    private User user;

    private Species species;

    private String profile;

    @NotNull
    private int dogRegNo;
    @NotNull
    private String dogNum;
    @NotNull
    private int birth;
    @NotNull
    private boolean sexNum;
    @NotNull
    private boolean neuterYn;
    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();
    @NotNull
    private int environment;
    @NotNull
    private int exercise;
    @NotNull
    private int foodCnt;
    @NotNull
    private int snackCnt;
    @NotNull
    private String protectorName;
    @NotNull
    private String protectorPhoneNum;


}
