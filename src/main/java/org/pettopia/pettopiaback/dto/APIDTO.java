package org.pettopia.pettopiaback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.pettopia.pettopiaback.domain.ConditionOfDefecation;

import java.time.LocalDate;
import java.util.List;

public class APIDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MapListRequest{
        private String name;
        private String address;
        private String phoneNumber;
        public static MapListRequest maps(String name, String address, String phoneNumber){
            return new MapListRequest(name, address, phoneNumber);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MapListResponse{
        private String name;
        private String address;
        private String phoneNumber;
    }





}
