package org.pettopia.pettopiaback.dto;

import lombok.*;
import jakarta.validation.constraints.NotNull;
import org.pettopia.pettopiaback.domain.ConditionOfDefecation;


public class ShotRecordsDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddShotRecordsRequest{
        @NotNull(message = "반려동물정보가 null이면 안됩니다.")
        private Long petPk;

        @NotNull(message = "접종 종류가 null이면 안됩니다")
        private String type;

        @NotNull(message = "접종 횟수가 null이면 안됩니다.")
        private int num;

        @NotNull(message = "접종 당시 나이가 null이면 안됩니다.")
        private int age;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShotRecordListsRequest{
        private Long pk;
        private String type;
        private int num;
        private int age;
        public static ShotRecordListsRequest Records(Long pk, String type, int num, int age){
            return new ShotRecordListsRequest(pk,type,num,age);
        }


    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ShotRecordsListResponse{
        private Long pk;
        private String type;
        private int num;
        private int age;
    }

}
