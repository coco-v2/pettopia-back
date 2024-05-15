package org.pettopia.pettopiaback.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.PetDTO;
import org.pettopia.pettopiaback.dto.PrincipalDetail;
import org.pettopia.pettopiaback.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/pet")
public class PetController {

    private final PetService petService;

    @Operation(summary = "반려동물 정보 등록", description = """
    [로그인 필요]
    201: 성공
    401: 실패
    <br>
    """)
    @PostMapping("/info/")
    public ResponseEntity makePetInfo(@AuthenticationPrincipal PrincipalDetail userDetails
            , @RequestBody @Valid PetDTO.PetInfoRequest petInfoRequest
    ) throws RuntimeException {

        String userId = (String) userDetails.getMemberInfo().get("socialId");
        log.info("userId",userId);

        petService.makePetInfo(userId, petInfoRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @Operation(summary = "내 반려동물 리스트 조회", description = """
    [로그인 필요]
    200: 성공
    <br>
    """)
    @GetMapping("/list/")
    public ResponseEntity<PetDTO.PetInfoListResponse> getPetInfoList(
            @AuthenticationPrincipal PrincipalDetail userDetails
    ) throws RuntimeException {

        String userId = (String) userDetails.getMemberInfo().get("socialId");
        log.info("userId",userId);


        List<PetDTO.PetShortInfoResponse> petInfoResponses
                = petService.getPetInfoList(userId);

        PetDTO.PetInfoListResponse response = PetDTO.PetInfoListResponse.builder()
                .cnt(petInfoResponses.size())
                .list(petInfoResponses)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "반려동물 정보 조회", description = """
    [로그인 필요]
    200: 성공
    <br>
    """)
    @GetMapping("/info/{petPk}")
    public ResponseEntity<PetDTO.PetInfoResponse> getPetInfo(@AuthenticationPrincipal PrincipalDetail userDetails
            , @PathVariable Long petPk
    ) throws RuntimeException {

        PetDTO.PetInfoResponse response = petService.getPetInfo(petPk);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "반려동물 정보 수정", description = """
    [로그인 필요]
    201: 성공
    401: 실패
    <br>
    """)
    @PatchMapping("/info/{petPk}")
    public ResponseEntity updatePetInfo(@AuthenticationPrincipal PrincipalDetail userDetails
            , @PathVariable Long petPk
            , @RequestBody @Valid PetDTO.PetInfoRequest petInfoRequest
    ) throws RuntimeException {

        petService.updatePetInfo(petPk, petInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @Operation(summary = "반려동물 추가정보 등록", description = """
    [로그인 필요]
    201: 성공
    401: 실패
    <br>
    """)
    @PostMapping("/extrainfo/{petPk}")
    public ResponseEntity makePetInfo(@AuthenticationPrincipal PrincipalDetail userDetails
            , @PathVariable Long petPk
            , @RequestBody @Valid PetDTO.PetExtraInfo petExtraInfoRequest
    ) throws RuntimeException {

        String userId = (String) userDetails.getMemberInfo().get("socialId");
        log.info("userId",userId);

        petService.makePetExtraInfo(petPk, petExtraInfoRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @Operation(summary = "반려동물 추가정보 수정", description = """
    [로그인 필요]
    201: 성공
    401: 실패
    <br>
    """)
    @PatchMapping("/extrainfo/{petPk}")
    public ResponseEntity updatePetInfo(@AuthenticationPrincipal PrincipalDetail userDetails
            , @PathVariable Long petPk
            , @RequestBody @Valid PetDTO.PetExtraInfo petExtraInfoRequest
    ) throws RuntimeException {

        String userId = (String) userDetails.getMemberInfo().get("socialId");
        log.info("userId",userId);

        petService.updatePetExtraInfo(petPk, petExtraInfoRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @Operation(summary = "반려동물 추가 정보 조회", description = """
    [로그인 필요]
    200: 성공
    <br>
    """)
    @GetMapping("/extrainfo/{petPk}")
    public ResponseEntity<PetDTO.PetExtraInfo> getPetExtraInfo(@AuthenticationPrincipal PrincipalDetail userDetails
            , @PathVariable Long petPk
    ) throws RuntimeException {

        PetDTO.PetExtraInfo response = petService.getPetExtraInfo(petPk);

        return ResponseEntity.ok(response);
    }



}
