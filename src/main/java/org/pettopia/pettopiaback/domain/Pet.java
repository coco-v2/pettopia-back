package org.pettopia.pettopiaback.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;
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
    private Integer dogRegNo; // 동물등록번호

    @NotNull
    private String dogNm; //이름

    @NotNull
    private Integer hair;

    @NotNull
    private Boolean sexNm; //성별

    @NotNull
    private Boolean neuterYn; //중성화 여부

    @NotNull
    private Integer birth; // 생년월일

    @NotNull
    private Float weight; // 몸무게

    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    private Integer environment; //생활환경

    private Integer exercise; //운동강도

    private Integer foodCnt; // 식사 횟수

    private Integer foodKind; // 식사 종류

    private Integer snackCnt; // 간식 종류

    @NotNull
    private String protectorName;

    @NotNull
    private String protectorPhoneNum;

    public void editInfo(String profile, String dogNm, Species species, Integer hair
            , Integer dogRegNo, Boolean sexNm, Boolean neuterYn, Integer birth
            , Float weight, String protectorName, String protectorPhoneNum) {
        this.profile = (profile != null)? profile : this.profile;
        this.dogNm = (dogNm != null)? dogNm : this.dogNm;
        this.species = (species != null)? species : this.species;
        this.dogRegNo = (dogRegNo != null)? dogRegNo : this.dogRegNo;
        this.hair = (hair != null)? hair : this.hair;
        this.sexNm = (sexNm != null)? sexNm : this.sexNm;
        this.neuterYn = (neuterYn != null)? neuterYn : this.neuterYn;
        this.birth = (birth != null)? birth : this.birth;
        this.weight = (weight != null)? weight : this.weight;
        this.protectorName = (protectorName != null)? protectorName : this.protectorName;
        this.protectorPhoneNum = (protectorPhoneNum != null)? protectorPhoneNum : this.protectorPhoneNum;
    }

    public void updateExtraInfo(Integer environment, Integer exercise, Integer foodCnt
            , Integer foodKind, Integer snackCnt) {
        this.environment = (environment != null)? environment : this.environment;
        this.exercise = (exercise != null)? exercise : this.exercise;
        this.foodCnt = (foodCnt != null)? foodCnt : this.foodCnt;
        this.foodKind = (foodKind != null)? foodKind : this.foodKind;
        this.snackCnt = (snackCnt != null)? snackCnt : this.snackCnt;
    }

}
