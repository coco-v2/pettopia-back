package org.pettopia.pettopiaback.controller;

import com.amazonaws.SdkClientException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.PrincipalDetail;
import org.pettopia.pettopiaback.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "S3 컨트롤러", description = "S3 관련 컨트롤러")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;

    @Operation(summary = "s3 url 받기")
    @PostMapping("/presigned")
    public ResponseEntity getS3PresignedKey(@AuthenticationPrincipal PrincipalDetail userDetails, @RequestBody Long petPk) {

        String preSignedUrl = s3Service.getPreSignedUrl(String.valueOf(petPk));

        Map<String, String> map = new HashMap<>();
        map.put("presigned_url", preSignedUrl);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "s3 파일 삭제")
    @DeleteMapping("/{fileUrl}")
    public ResponseEntity deleteFile(@PathVariable String fileUrl) throws IOException {
        try {
            s3Service.fileDelete(fileUrl);
            return ResponseEntity.ok().build();
        } catch (SdkClientException e) {
            throw new IOException("Error deleting file from S3", e);
        }
    }



}