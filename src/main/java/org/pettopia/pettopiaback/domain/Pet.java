package org.pettopia.pettopiaback.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "pet")
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private int hair;

    @NotNull
    private boolean sexNm; //성별

    @NotNull
    private boolean neuterYn; //중성화 여부

    @NotNull
    private int birth;
    
    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    private int environment;

    private int exercise;

    private int foodCnt;

    private int snackCnt;

    private String protectorName;

    private String protectorPhoneNum;

    private int foodKind;

}
