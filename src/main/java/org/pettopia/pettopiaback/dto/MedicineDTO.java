package org.pettopia.pettopiaback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

public class MedicineDTO {

    @AllArgsConstructor
    @Getter
    public static class RequestMedicine {
        @NotNull(message = "약 이름이 null이면 안됩니다.")
        String name;
        @NotNull(message = "약 개수가 null이면 안됩니다.")
        Integer cnt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseMedicineList {
        private int cnt;
        private List<MedicineDTO.RequestMedicine> list;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RequestMedicineList {
        private List<MedicineDTO.RequestMedicine> list;
    }

}
