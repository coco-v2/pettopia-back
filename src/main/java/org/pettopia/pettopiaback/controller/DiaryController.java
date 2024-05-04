package org.pettopia.pettopiaback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.DiaryDTO;
import org.pettopia.pettopiaback.service.DiaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/diary/")
    public ResponseEntity makeDiary(@RequestBody @Valid DiaryDTO.AddDiaryRequest addDiaryRequest){
         diaryService.makeDiary(addDiaryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
