package org.pettopia.pettopiaback.dto;

import lombok.*;

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