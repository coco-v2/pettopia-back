package org.pettopia.pettopiaback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.DiaryDTO;
import org.pettopia.pettopiaback.dto.PrincipalDetail;
import org.pettopia.pettopiaback.exception.ForbiddenException;
import org.pettopia.pettopiaback.exception.NotFoundException;
import org.pettopia.pettopiaback.service.DiaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Tag(name = "다이어리 컨트롤러", description = "다이어리 관련 컨트롤러")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/life")
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "다이어리 작성", description = """
    [로그인 필요]
    <br>
    """)
    @PostMapping("/diary")
    public ResponseEntity makeDiary(@AuthenticationPrincipal PrincipalDetail userDetails,
                                    Long petPk,
                                    @RequestBody @Valid DiaryDTO.DiaryRequest addDiaryRequest){
        diaryService.makeDiary(petPk, addDiaryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "다이어리 조회", description = """
    [로그인 필요]
    <br>
    """)
    @GetMapping("/diary/{diaryPk}")
    public ResponseEntity<DiaryDTO.DiaryResponse> getDiary(@AuthenticationPrincipal PrincipalDetail userDetails
            , @PathVariable Long diaryPk
    ) throws RuntimeException {

        DiaryDTO.DiaryResponse response = diaryService.getDiary(diaryPk);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "다이어리 삭제", description = """
    [로그인 필요]
    <br>
    """)
    @DeleteMapping("/diary/{diaryPk}")
    public ResponseEntity deleteDiary(@AuthenticationPrincipal PrincipalDetail userDetails
            , @PathVariable Long diaryPk
    ) throws NoSuchElementException, ForbiddenException {
        try{
            diaryService.delete(diaryPk);
            return ResponseEntity.noContent().build();
        }
        catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "다이어리 수정", description = """
    [로그인 필요]
    <br>
    """)
    @PatchMapping("/diary/{diaryPk}")
    public ResponseEntity<DiaryDTO.DiaryResponse> updateDiary(@AuthenticationPrincipal PrincipalDetail userDetails
            , @PathVariable Long diaryPk
            , @RequestBody @Valid DiaryDTO.DiaryRequest diaryRequest
    ) throws NoSuchElementException, ForbiddenException {
        try {
            DiaryDTO.DiaryResponse response = diaryService.updateDiary(diaryPk, diaryRequest);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }


}
