package org.pettopia.pettopiaback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.pettopia.pettopiaback.domain.Pet;
import org.pettopia.pettopiaback.domain.ShotRecords;
import org.pettopia.pettopiaback.domain.Users;
import org.pettopia.pettopiaback.dto.ShotRecordsDTO;
import org.pettopia.pettopiaback.exception.NotFoundException;
import org.pettopia.pettopiaback.repository.PetRepository;
import org.pettopia.pettopiaback.repository.ShotRecordsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ShotRecordsService {
    private final PetRepository petRepository;
    private final ShotRecordsRepository shotRecordsRepository;

    public ShotRecords makeShotRecords(ShotRecordsDTO.AddShotRecordsRequest addShotRecordsRequest) throws RuntimeException{
        Pet findPet = petRepository.findById(addShotRecordsRequest.getPetPk())
                .orElseThrow(() -> new NotFoundException("반려동물 정보가 없습니다"));

        ShotRecords shotRecords = ShotRecords.makeShotRecord(findPet, addShotRecordsRequest.getType(), addShotRecordsRequest.getNum(), addShotRecordsRequest.getAge());
        return shotRecordsRepository.save(shotRecords);
    }

    public void delete(Long pk) throws RuntimeException{
        ShotRecords shotRecords = shotRecordsRepository.findById(pk)
                .orElseThrow(()-> new NotFoundException("해당 차트가 존재하지 않습니다"));
        shotRecordsRepository.delete(shotRecords);

    }

    public ShotRecordsDTO.ShotRecordsListResponse getShotRecords(ShotRecordsDTO.ShotRecordListsRequest request){
        ShotRecordsDTO.ShotRecordsListResponse response =
                ShotRecordsDTO.ShotRecordsListResponse.builder()
                        .pk(request.getPk())
                        .petName(request.getPetName())
                        .petPk(request.getPetPk())
                        .type(request.getType())
                        .num(request.getNum())
                        .age(request.getAge())
                        .build();
        return response;
    }


    public List<ShotRecordsDTO.ShotRecordsListResponse> getShotRecordsList(Users user) {
        List<Pet> petList = petRepository.findAllByUsers(user);
        List<ShotRecords> shotRecordsByUser = new ArrayList<>();
        for (Pet pet : petList) {
            List<ShotRecords> shotRecordByPet = shotRecordsRepository.findByPet(pet);
            shotRecordsByUser.addAll(shotRecordByPet);
        }
        List<ShotRecordsDTO.ShotRecordsListResponse> shotRecordsDTOS = new ArrayList<>();
        for (ShotRecords shotRecords : shotRecordsByUser) {
            ShotRecordsDTO.ShotRecordListsRequest request =
                    ShotRecordsDTO.ShotRecordListsRequest.Records(
                            shotRecords.getPk(),
                            shotRecords.getPet().getPk(),
                            shotRecords.getPet().getDogNm(),
                            shotRecords.getType(),
                            shotRecords.getNum(),
                            shotRecords.getAge()
                    );
            ShotRecordsDTO.ShotRecordsListResponse response = getShotRecords(request);
            ShotRecordsDTO.ShotRecordsListResponse shotRecordDTO =
                    ShotRecordsDTO.ShotRecordsListResponse.builder()
                            .pk(response.getPk())
                            .petPk(response.getPetPk())
                            .petName(request.getPetName())
                            .type(response.getType())
                            .num(response.getNum())
                            .age(response.getAge())
                            .build();
            shotRecordsDTOS.add(shotRecordDTO);
        }
        return shotRecordsDTOS;
    }

    public ShotRecords modifyShotRecords(Long recordPk, ShotRecordsDTO.AddShotRecordsRequest addShotRecordsRequest) throws RuntimeException{
        ShotRecords shotRecords = shotRecordsRepository.findById(recordPk)
                .orElseThrow(() -> new NotFoundException("해당 접종 기록이 존재하지 않음"));
        System.out.println("반려동물 정보"+addShotRecordsRequest.getPetPk());
        System.out.println("타입"+addShotRecordsRequest.getType());
        Pet pet = petRepository.findById(addShotRecordsRequest.getPetPk())
                .orElseThrow(() -> new NotFoundException("해당 반려동물이이 존재하지 않음"));
        shotRecords.modifyShotRecord(pet, addShotRecordsRequest.getType(), addShotRecordsRequest.getNum(), addShotRecordsRequest.getAge());
        return shotRecords;
    }


}
