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
    private Integer birth;

    @NotNull
    private Float weight;

    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    private Integer environment;

    private Integer exercise;

    private Integer foodCnt;

    private Integer snackCnt;

    @NotNull
    private String protectorName;

    @NotNull
    private String protectorPhoneNum;

    private Integer foodKind;

    public void edit(String profile, String dogNm, Species species, Integer hair
            , Integer dogRegNo, Boolean sexNm, Boolean neuterYn, Integer birth
            , Float weight, String protectorName, String protectorPhoneNum) {
        this.profile = (profile != null)? profile : this.profile;
        this.dogNm = (dogNm != null)? dogNm : this.dogNm;
        this.species = (species != null)? species : this.species;
        this.hair = (hair != null)? hair : this.hair;
        this.sexNm = (sexNm != null)? sexNm : this.sexNm;
        this.neuterYn = (neuterYn != null)? neuterYn : this.neuterYn;
        this.birth = (birth != null)? birth : this.birth;
        this.weight = (weight != null)? weight : this.weight;
        this.protectorName = (protectorName != null)? protectorName : this.protectorName;
        this.protectorPhoneNum = (protectorPhoneNum != null)? protectorPhoneNum : this.protectorPhoneNum;
    }

}
