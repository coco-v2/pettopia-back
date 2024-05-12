package org.pettopia.pettopiaback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.domain.Users;
import org.pettopia.pettopiaback.dto.PetDTO;
import org.pettopia.pettopiaback.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/pet")
public class PetController {

    private final PetService petService;

    @PostMapping("/info/")
    public ResponseEntity makePetInfo(String userId, @RequestBody @Valid PetDTO.AddPetInfoRequest addPetInfoRequest
    ) throws RuntimeException {

        petService.makePetInfo(userId, addPetInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/info/")
    public ResponseEntity<PetDTO.PetInfoResponse> getPetInfo(String userId){

        PetDTO.PetInfoResponse petInfoResponse = petService.getPetInfo(userId);

        return ResponseEntity.ok(petInfoResponse);
    }

}
