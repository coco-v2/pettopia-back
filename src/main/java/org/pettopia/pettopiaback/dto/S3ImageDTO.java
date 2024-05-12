package org.pettopia.pettopiaback.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class S3ImageDTO {
    private String folderName;
    private String imageName;

    public S3ImageDTO(String folderName, String imageName) {
        this.folderName = folderName;
        this.imageName = imageName;
    }
}