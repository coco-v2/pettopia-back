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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_pk")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_pk")
    private Species species;

    private String profile;

    @NotNull
    private int dogRegNo; // 동물등록번호

    @NotNull
    private String dogNm; //이름

    @NotNull
    private int birth;

    @NotNull
    private boolean sexNm; //성별

    @NotNull
    private boolean neuterYn; //중성화 여부
    
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

    @NotNull
    private int hair;

    @NotNull
    private int foodKind;

}
