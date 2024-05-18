package org.pettopia.pettopiaback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.pettopia.pettopiaback.domain.ConditionOfDefecation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class DiaryDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryRequest {

        @NotNull(message = "식사 횟수가 null이면 안됩니다.")
        private Integer mealCnt;

        @NotNull(message = "간식 횟수가 null이면 안됩니다.")
        private Integer snackCnt;

        private List<MedicineDTO.RequestMedicine> medicineList;

        @NotNull(message = "산책 횟수가 null이면 안됩니다.")
        private Integer walkCnt;

        private ConditionOfDefecation conditionOfDefecation;

        private String defecationText;

        private String etc;

        private LocalDate calendarDate;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryUpdateRequest {

        @NotNull(message = "식사 횟수가 null이면 안됩니다.")
        private Integer mealCnt;

        @NotNull(message = "간식 횟수가 null이면 안됩니다.")
        private Integer snackCnt;

        private List<MedicineDTO.RequestMedicine> medicineList;

        @NotNull(message = "산책 횟수가 null이면 안됩니다.")
        private Integer walkCnt;

        private ConditionOfDefecation conditionOfDefecation;

        private String defecationText;

        private String etc;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DiaryResponse{

        private Integer mealCont;

        private Integer snackCnt;

        private Integer walkCnt;

        private ConditionOfDefecation conditionOfDefecation;

        private String defecationText;

        private String etc;

        private MedicineDTO.ResponseMedicineList medicineList;

        private String calendarDate;



    }

}
