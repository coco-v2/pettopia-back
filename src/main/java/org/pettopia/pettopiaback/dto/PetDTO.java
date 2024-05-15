package org.pettopia.pettopiaback.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.pettopia.pettopiaback.domain.Species;

import java.time.LocalDateTime;
import java.util.List;

public class PetDTO {

    @AllArgsConstructor
    @Getter
    public static class AddPetInfoRequest {

        @NotNull(message = "반려동물 등록 번호가 null이면 안됩니다.")
        private int dogRegNo;

        @NotNull(message = "반려동물 이름이 null이면 안됩니다.")
        private String dogNm;

        @NotNull(message = "품종 pk가 null이면 안됩니다.")
        private Long speciesPk;

        private String profile;

        @NotNull(message = "단장모가 null이면 안됩니다.")
        private int hair;

        @NotNull(message = "성별이 null이면 안됩니다.")
        private boolean sexNm;

        @NotNull(message = "중성화 여부가 null이면 안됩니다.")
        private boolean neuterYn;

        @NotNull(message = "생년월일이 null이면 안됩니다.")
        private int birth;

        @NotNull(message = "몸무게가 null이면 안됩니다.")
        private float weight;

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

        private int dogRegNo; // 동물등록번호

        private String dogNm; //이름

        private int hair;

        private boolean sexNm; //성별

        private boolean neuterYn; //중성화 여부

        private int birth;

        private float weight;

        private String protectorName;

        private String protectorPhoneNum;

    }
}