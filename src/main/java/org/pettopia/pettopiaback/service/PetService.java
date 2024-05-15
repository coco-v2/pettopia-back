package org.pettopia.pettopiaback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.domain.Pet;
import org.pettopia.pettopiaback.domain.Species;
import org.pettopia.pettopiaback.domain.Users;
import org.pettopia.pettopiaback.dto.PetDTO;
import org.pettopia.pettopiaback.exception.NotFoundException;
import org.pettopia.pettopiaback.repository.PetRepository;
import org.pettopia.pettopiaback.repository.SpeciesRepository;
import org.pettopia.pettopiaback.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class PetService {

    private final PetRepository petRepository;
    private final SpeciesRepository speciesRepository;
    private final UserRepository userRepository;

    public Pet makePetInfo(String userId, PetDTO.AddPetInfoRequest addPetInfoRequest
    ) throws RuntimeException {

        Users user = userRepository.findBySocialId(userId)
                .orElseThrow(() -> new NotFoundException("해당하는 사용자가 없습니다."));


        Species species = speciesRepository.findById(addPetInfoRequest.getSpeciesPk())
                .orElseThrow(()-> new NotFoundException("해당 품종이 존재하지 않습니다."));

        String profile = addPetInfoRequest.getProfile() != null ? addPetInfoRequest.getProfile() : "";

        Pet pet = Pet.builder()
                .users(user)
                .dogRegNo(addPetInfoRequest.getDogRegNo())
                .dogNm(addPetInfoRequest.getDogNm())
                .species(species)
                .profile(profile)
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

    public PetDTO.PetInfoResponse getPetInfo(String userId) {

        Users user = userRepository.findBySocialId(userId)
                .orElseThrow(() -> new NotFoundException("해당하는 사용자가 없습니다."));

        Pet pet = petRepository.findByUsers(user);

        PetDTO.PetInfoResponse petInfoResponse = PetDTO.PetInfoResponse.builder()
                .profile(pet.getProfile())
                .dogNm(pet.getDogNm())
                .speciesPk(pet.getSpecies().getPk())
                .dogRegNo(pet.getDogRegNo())
                .hair(pet.getHair())
                .sexNm(pet.isSexNm())
                .neuterYn(pet.isNeuterYn())
                .birth(pet.getBirth())
                .weight(pet.getWeight())
                .protectorName(pet.getProtectorName())
                .protectorPhoneNum(pet.getProtectorPhoneNum())
                .build();

        return petInfoResponse;
    }

    public List<PetDTO.PetInfoResponse> getPetInfoList(String userId) {

        Users user = userRepository.findBySocialId(userId)
                .orElseThrow(() -> new NotFoundException("해당하는 사용자가 없습니다."));

        List<Pet> pets = petRepository.findAllByUsers(user);

        List<PetDTO.PetInfoResponse> petInfoList = new ArrayList<>();

        for (Pet pet : pets) {
            PetDTO.PetInfoResponse petInfoResponse = PetDTO.PetInfoResponse.builder()
                    .profile(pet.getProfile())
                    .dogNm(pet.getDogNm())
                    .speciesPk(pet.getSpecies().getPk())
                    .dogRegNo(pet.getDogRegNo())
                    .hair(pet.getHair())
                    .sexNm(pet.isSexNm())
                    .neuterYn(pet.isNeuterYn())
                    .birth(pet.getBirth())
                    .weight(pet.getWeight())
                    .protectorName(pet.getProtectorName())
                    .protectorPhoneNum(pet.getProtectorPhoneNum())
                    .build();
            petInfoList.add(petInfoResponse);
        }

        return petInfoList;


    }
}
