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
@Builder
@Table(name = "diary")
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_pk")
    private Pet pet;

    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    @NotNull
    private Integer mealCnt;

    @NotNull
    private Integer snackCnt;

    @NotNull
    private Integer walkCnt;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_of_defecation")
    private ConditionOfDefecation conditionOfDefecation;

    private String defecationText;

    private String etc;

    @NotNull
    @CreationTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt = LocalDateTime.now();


    public void updateInfo(Integer mealCnt, Integer snackCnt, Integer walkCnt
            , ConditionOfDefecation conditionOfDefecation, String defecationText, String etc) {
        this.mealCnt = (mealCnt != null)? mealCnt : this.mealCnt;
        this.snackCnt = (snackCnt != null)? snackCnt : this.snackCnt;
        this.walkCnt = (walkCnt != null)? walkCnt : this.walkCnt;
        this.conditionOfDefecation = (conditionOfDefecation != null)? conditionOfDefecation : this.conditionOfDefecation;
        this.defecationText = (defecationText != null)? defecationText : this.defecationText;
        this.etc = (etc != null)? etc : this.etc;
        this.updateAt = LocalDateTime.now();
    }
}
