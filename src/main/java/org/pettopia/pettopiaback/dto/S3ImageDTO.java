package org.pettopia.pettopiaback.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class S3ImageDTO {
    private String imageName;

    public S3ImageDTO(String imageName) {
        this.imageName = imageName;
    }
}