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
import java.time.format.DateTimeFormatter;
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


    public Pet makePetInfo(String userId, PetDTO.PetInfoRequest addPetInfoRequest
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
                .sexNm(addPetInfoRequest.getSexNm())
                .neuterYn(addPetInfoRequest.getNeuterYn())
                .birth(addPetInfoRequest.getBirth())
                .weight(addPetInfoRequest.getWeight())
                .protectorName(addPetInfoRequest.getProtectorName())
                .protectorPhoneNum(addPetInfoRequest.getProtectorPhoneNum())
                .createAt(LocalDateTime.now())
                .build();

        return petRepository.save(pet);
    }

    public PetDTO.PetInfoResponse getPetInfo(Long petPk) {

        Pet pet = petRepository.findById(petPk)
                .orElseThrow(() -> new NotFoundException("해당하는 반려동물이 없습니다."));

        PetDTO.PetInfoResponse petInfoResponse = PetDTO.PetInfoResponse.builder()
                .speciesName(pet.getSpecies().getName())
                .profile(pet.getProfile())
                .dogRegNo(pet.getDogRegNo())
                .dogNm(pet.getDogNm())
                .hair(pet.getHair())
                .sexNm(pet.getSexNm())
                .neuterYn(pet.getNeuterYn())
                .birth(pet.getBirth())
                .weight(pet.getWeight())
                .protectorName(pet.getProtectorName())
                .protectorPhoneNum(pet.getProtectorPhoneNum())
                .build();

        return petInfoResponse;
    }

    public List<PetDTO.PetShortInfoResponse> getPetInfoList(String userId) {

        Users user = userRepository.findBySocialId(userId)
                .orElseThrow(() -> new NotFoundException("해당하는 사용자가 없습니다."));

        List<Pet> pets = petRepository.findAllByUsers(user);

        List<PetDTO.PetShortInfoResponse> petInfoList = new ArrayList<>();

        for (Pet pet : pets) {
            PetDTO.PetShortInfoResponse petInfoResponse = PetDTO.PetShortInfoResponse.builder()
                    .petPk(pet.getPk())
                    .dogNm(pet.getDogNm())
                    .build();

            petInfoList.add(petInfoResponse);
        }
        return petInfoList;
    }

    public void updatePetInfo(Long petPk, PetDTO.PetInfoRequest petInfoRequest) {

        Pet pet = petRepository.findById(petPk)
                .orElseThrow(() -> new NotFoundException("해당하는 반려동물이 없습니다."));

        Species species = speciesRepository.findById(petInfoRequest.getSpeciesPk())
                .orElseThrow(() -> new NotFoundException("해당하는 반려동물의 종류를 찾을 수 없습니다."));

        pet.updateInfo(
                petInfoRequest.getProfile(), petInfoRequest.getDogNm(), species, petInfoRequest.getHair()
                , petInfoRequest.getDogRegNo(), petInfoRequest.getSexNm(), petInfoRequest.getNeuterYn()
                , petInfoRequest.getBirth(), petInfoRequest.getWeight(), petInfoRequest.getProtectorName()
                , petInfoRequest.getProtectorPhoneNum()
        );

        petRepository.save(pet);

    }

    public void makePetExtraInfo(Long petPk, PetDTO.PetExtraInfo petExtraInfo
    ) throws  RuntimeException {

        Pet pet = petRepository.findById(petPk)
                .orElseThrow(() -> new NotFoundException("해당하는 반려동물이 없습니다."));

        pet.updateExtraInfo(
                petExtraInfo.getEnvironment(), petExtraInfo.getExercise()
                , petExtraInfo.getFoodCnt(), petExtraInfo.getFoodKind()
                , petExtraInfo.getSnackCnt()
        );

        petRepository.save(pet);

    }

    public void updatePetExtraInfo(Long petPk, PetDTO.PetExtraInfo petExtraInfo
    ) throws  RuntimeException {

        Pet pet = petRepository.findById(petPk)
                .orElseThrow(() -> new NotFoundException("해당하는 반려동물이 없습니다."));

        pet.updateExtraInfo(
                petExtraInfo.getEnvironment(), petExtraInfo.getExercise()
                , petExtraInfo.getFoodCnt(), petExtraInfo.getFoodKind()
                , petExtraInfo.getSnackCnt()
        );


        petRepository.save(pet);

    }

    public PetDTO.PetExtraInfo getPetExtraInfo(Long petPk) throws RuntimeException {

        Pet pet = petRepository.findById(petPk)
                .orElseThrow(() -> new NotFoundException("해당하는 반려동물이 없습니다."));

        PetDTO.PetExtraInfo petExtraInfo = PetDTO.PetExtraInfo.builder()
                .environment(pet.getEnvironment())
                .exercise(pet.getExercise())
                .foodCnt(pet.getFoodCnt())
                .foodKind(pet.getFoodKind())
                .snackCnt(pet.getSnackCnt())
                .build();

        return petExtraInfo;


    }

    public PetDTO.PetRegistrationResponse getPetRegistration(Long petPk) throws RuntimeException {

        Pet pet = petRepository.findById(petPk)
                .orElseThrow(() -> new NotFoundException("해당하는 반려동물이 없습니다."));

        String createAt = pet.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        PetDTO.PetRegistrationResponse petRegistrationResponse = PetDTO.PetRegistrationResponse.builder()
                .speciesName(pet.getSpecies().getName())
                .profile(pet.getProfile())
                .dogRegNo(pet.getDogRegNo())
                .dogNm(pet.getDogNm())
                .sexNm(pet.getSexNm())
                .neuterYn(pet.getNeuterYn())
                .birth(pet.getBirth())
                .protectorName(pet.getProtectorName())
                .protectorPhoneNum(pet.getProtectorPhoneNum())
                .createAt(createAt)
                .build();

        return petRegistrationResponse;

    }
}

