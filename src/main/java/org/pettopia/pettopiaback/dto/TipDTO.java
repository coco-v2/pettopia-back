package org.pettopia.pettopiaback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pettopia.pettopiaback.domain.TipCategory;

import java.util.List;

public class TipDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ResponseTipList{
        private int cnt;
        private List<Response> responses;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response{
        private String content;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class Request {
        private Long petTypePk;
        private TipCategory tipCategory;
    }

}
