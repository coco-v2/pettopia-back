package org.pettopia.pettopiaback.dto;

import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PetDTO {

    @AllArgsConstructor
    @Getter
    public static class PetInfoRequest {

        private String profile;

        @NotNull(message = "반려동물 이름이 null이면 안됩니다.")
        private String dogNm;

        @NotNull(message = "반려동물 등록 번호가 null이면 안됩니다.")
        private Integer dogRegNo;

        @NotNull(message = "품종 pk가 null이면 안됩니다.")
        private Long speciesPk;

        @NotNull(message = "단장모가 null이면 안됩니다.")
        private Integer hair;

        @NotNull(message = "성별이 null이면 안됩니다.")
        private Boolean sexNm;

        @NotNull(message = "중성화 여부가 null이면 안됩니다.")
        private Boolean neuterYn;

        @NotNull(message = "생년월일이 null이면 안됩니다.")
        private Integer birth;

        @NotNull(message = "몸무게가 null이면 안됩니다.")
        private Float weight;

        @NotNull(message = "보호자 이름이 null이면 안됩니다.")
        private String protectorName;

        @NotNull(message = "보호자 연락처가 null이면 안됩니다.")
        private String protectorPhoneNum;

    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class PetShortInfoResponse {
        private Long petPk;
        private String dogNm; // 이름
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PetInfoListResponse {
        private int cnt;
        private List<PetShortInfoResponse> list;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class PetInfoResponse {

        private String speciesName;

        private String profile;

        private Integer dogRegNo; // 동물등록번호

        private String dogNm; //이름

        private Integer hair;

        private Boolean sexNm; //성별

        private Boolean neuterYn; //중성화 여부

        private Integer birth;

        private Float weight;

        private String protectorName;

        private String protectorPhoneNum;

    }
}

