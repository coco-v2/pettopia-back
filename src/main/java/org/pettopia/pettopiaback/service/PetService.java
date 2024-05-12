package org.pettopia.pettopiaback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.domain.Pet;
import org.pettopia.pettopiaback.domain.Species;
import org.pettopia.pettopiaback.dto.PetInfoDTO;
import org.pettopia.pettopiaback.exception.NotFoundException;
import org.pettopia.pettopiaback.repository.PetRepository;
import org.pettopia.pettopiaback.repository.SpeciesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class PetService {

    private final PetRepository petRepository;
    private final SpeciesRepository speciesRepository;

    public Pet makePetInfo(PetInfoDTO.AddPetInfoRequest addPetInfoRequest) throws RuntimeException {

        Species species = speciesRepository.findById(addPetInfoRequest.getSpeciesPk())
                .orElseThrow(()-> new NotFoundException("해당 품종이 존재하지 않습니다."));

        Pet pet = Pet.builder()
                .dogRegNo(addPetInfoRequest.getDogRegNo())
                .dogNm(addPetInfoRequest.getDogNm())
                .species(species)
                .hair(addPetInfoRequest.getHair())
                .sexNm(addPetInfoRequest.isSexNm())
                .neuterYn(addPetInfoRequest.isNeuterYn())
                .birth(addPetInfoRequest.getBirth())
                .weight(addPetInfoRequest.getWeight())
                .protectorName(addPetInfoRequest.getProtectorName())
                .protectorPhoneNum(addPetInfoRequest.getProtectorPhoneNum())
                .createAt(LocalDateTime.now())
                .build();

        return petRepository.save(pet);
    }
}
