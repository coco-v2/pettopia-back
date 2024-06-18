package org.pettopia.pettopiaback.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.PrincipalDetail;
import org.pettopia.pettopiaback.dto.ShotRecordsDTO;
import org.pettopia.pettopiaback.exception.NotFoundException;
import org.pettopia.pettopiaback.service.ShotRecordsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "예방접종 컨트롤러")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class ShotRecordsController {

    private final ShotRecordsService shotRecordsService;

    @PostMapping("/shot_records/")
    public ResponseEntity makeShotRecords(@RequestBody @Valid ShotRecordsDTO.AddShotRecordsRequest addDShotRecordsRequest){
        shotRecordsService.makeShotRecords(addDShotRecordsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/shot_records/")
    public ResponseEntity<List<ShotRecordsDTO.ShotRecordsListResponse>> getShotRecordsList(@AuthenticationPrincipal PrincipalDetail userDetails){

        String userId = (String)  userDetails.getMemberInfo().get("socialId");
        log.info("userId",userId);

        List<ShotRecordsDTO.ShotRecordsListResponse> shotRecordsList = shotRecordsService.getShotRecordsList(userId);
        return ResponseEntity.ok(shotRecordsList);
    }

    @DeleteMapping("/shot_records/{shot_records_id}")
    public ResponseEntity deleteShotRecord(@PathVariable Long shot_records_id){
        try{
            shotRecordsService.delete(shot_records_id);
            return ResponseEntity.noContent().build();
        }
        catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/shot_records/{shot_records_id}")
    public ResponseEntity modifyShotRecord(@PathVariable Long shot_records_id ,@RequestBody @Valid ShotRecordsDTO.AddShotRecordsRequest addShotRecordsRequest){
        shotRecordsService.modifyShotRecords(shot_records_id,addShotRecordsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
}
