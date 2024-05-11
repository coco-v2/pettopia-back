package org.pettopia.pettopiaback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.PetInfoDTO;
import org.pettopia.pettopiaback.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/pet")
public class PetController {

    private final PetService petService;

    @PostMapping("/info/")
    public ResponseEntity makePetInfo(@RequestBody @Valid PetInfoDTO.AddPetInfoRequest addPetInfoRequest){
        petService.makePetInfo(addPetInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
