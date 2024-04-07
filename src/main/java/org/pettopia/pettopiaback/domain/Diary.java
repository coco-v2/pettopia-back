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

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "diary")
public class Diary {

    @Id
    @GeneratedValue
    private Long pk;

    private Pet pet;

    @NotNull
    private LocalDateTime createAt;

    @NotNull
    private int mealCnt;

    @NotNull
    private int snackCnt;

    @NotNull
    private int walkCnt;

    private ConditionOfDefecation conditionOfDefecation;

    private String defecationText;

}
