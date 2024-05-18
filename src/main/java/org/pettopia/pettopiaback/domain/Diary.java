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

}
