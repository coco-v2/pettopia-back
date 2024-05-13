package org.pettopia.pettopiaback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.domain.Users;
import org.pettopia.pettopiaback.dto.PetDTO;
import org.pettopia.pettopiaback.dto.PrincipalDetail;
import org.pettopia.pettopiaback.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/pet")
public class PetController {

    private final PetService petService;

    @PostMapping("/info/")
    public ResponseEntity makePetInfo(@AuthenticationPrincipal PrincipalDetail userDetails
            , @RequestBody @Valid PetDTO.AddPetInfoRequest addPetInfoRequest
    ) throws RuntimeException {
        String userId = (String) userDetails.getMemberInfo().get("socialId");

        petService.makePetInfo(userId, addPetInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/info/")
    public ResponseEntity<PetDTO.PetInfoListResponse> getPetInfoList(
            @AuthenticationPrincipal PrincipalDetail userDetails
    ) throws RuntimeException {

        String userId = (String) userDetails.getMemberInfo().get("socialId");

        List<PetDTO.PetInfoResponse> petInfoResponses
                = petService.getPetInfoList(userId);

        PetDTO.PetInfoListResponse response = PetDTO.PetInfoListResponse.builder()
                .cnt(petInfoResponses.size())
                .list(petInfoResponses)
                .build();

        return ResponseEntity.ok(response);
    }
//    public ResponseEntity<PetDTO.PetInfoResponse> getPetInfo(
//            @AuthenticationPrincipal PrincipalDetail userDetails
//    ) throws RuntimeException {
//
//        String userId = (String) userDetails.getMemberInfo().get("socialId");
//
//
//        PetDTO.PetInfoResponse petInfoResponse = petService.getPetInfo(userId);
//
//        return ResponseEntity.ok(petInfoResponse);
//    }
//


}
