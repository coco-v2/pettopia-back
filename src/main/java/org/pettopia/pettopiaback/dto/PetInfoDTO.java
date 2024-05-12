package org.pettopia.pettopiaback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;
import org.pettopia.pettopiaback.domain.Species;

public class PetInfoDTO {

    @AllArgsConstructor
    @Getter
    public static class AddPetInfoRequest {

        @NotNull(message = "반려동물 등록 번호가 null이면 안됩니다.")
        private int dogRegNo;

        @NotNull(message = "반려동물 이름이 null이면 안됩니다.")
        private String dogNm;

        @NotNull(message = "품종 pk가 null이면 안됩니다.")
        private Long speciesPk;

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
}
