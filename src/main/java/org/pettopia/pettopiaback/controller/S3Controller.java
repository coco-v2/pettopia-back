package org.pettopia.pettopiaback.controller;

import com.amazonaws.SdkClientException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.domain.Users;
import org.pettopia.pettopiaback.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "S3Controller", description = "S3 관련 컨트롤러입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/presigned")
    public ResponseEntity getS3PresignedKey(@AuthenticationPrincipal Users user) {
        String preSignedUrl = s3Service.getPreSignedUrl(user);

        Map<String, String> map = new HashMap<>();
        map.put("presigned_url", preSignedUrl);

        return ResponseEntity.ok(map);
    }

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