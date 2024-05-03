package org.pettopia.pettopiaback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.pettopia.pettopiaback.domain.ConditionOfDefecation;


public class DiaryDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddDiaryRequest{
        @NotNull(message = "반려동물정보가 null이면 안됩니다.")
        private Long petPk;

        @NotNull(message = "식사 횟수가 null이면 안됩니다.")
        private int mealCont;

        @NotNull(message = "간식 횟수가 null이면 안됩니다.")
        private int snackCnt;

        @NotNull(message = "산책 횟수가 null이면 안됩니다.")
        private int walkCnt;

        private ConditionOfDefecation conditionOfDefecation;
        private String defecationText;


    }
}
