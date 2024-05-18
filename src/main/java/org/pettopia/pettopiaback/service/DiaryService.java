package org.pettopia.pettopiaback.service;


import org.pettopia.pettopiaback.domain.DiaryMedicine;
import org.pettopia.pettopiaback.domain.Medicine;
import org.pettopia.pettopiaback.dto.DiaryDTO;
import org.pettopia.pettopiaback.domain.Diary;
import org.pettopia.pettopiaback.domain.Pet;
import org.pettopia.pettopiaback.dto.MedicineDTO;
import org.pettopia.pettopiaback.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.repository.DiaryMedicineRepository;
import org.pettopia.pettopiaback.repository.MedicineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.pettopia.pettopiaback.repository.DiaryRepository;
import org.pettopia.pettopiaback.repository.PetRepository;

import java.time.LocalDateTime;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

    private final PetRepository petRepository;
    private final DiaryRepository diaryRepository;
    private final MedicineRepository medicineRepository;
    private final DiaryMedicineRepository diaryMedicineRepository;

    public void makeDiary(Long petPk, DiaryDTO.AddDiaryRequest addDiaryRequest) throws RuntimeException{

        Pet findPet = petRepository.findById(petPk)
                .orElseThrow(() -> new NotFoundException("반려동물 정보가 없습니다"));

        Diary diary = Diary.builder()
                .pet(findPet)
                .mealCnt(addDiaryRequest.getMealCont())
                .snackCnt(addDiaryRequest.getSnackCnt())
                .walkCnt(addDiaryRequest.getWalkCnt())
                .conditionOfDefecation(addDiaryRequest.getConditionOfDefecation())
                .defecationText(addDiaryRequest.getDefecationText())
                .createAt(LocalDateTime.now())
                .build();

        diaryRepository.save(diary);

        for (MedicineDTO.RequestMedicine requestMedicine : addDiaryRequest.getMedicineList()) {
            Medicine medicine = medicineRepository.findByName(requestMedicine.getName())
                    .orElseThrow(() -> new NotFoundException("약 정보가 없습니다."));

            DiaryMedicine diaryMedicine = DiaryMedicine.builder()
                    .diary(diary)
                    .medicine(medicine)
                    .medicineCnt(requestMedicine.getCnt())
                    .build();

            diaryMedicineRepository.save(diaryMedicine);
        }


    }
}
