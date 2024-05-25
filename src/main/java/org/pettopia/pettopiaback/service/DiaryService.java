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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

    private final PetRepository petRepository;
    private final DiaryRepository diaryRepository;
    private final MedicineRepository medicineRepository;
    private final DiaryMedicineRepository diaryMedicineRepository;
    private final MedicineService medicineService;


    public void makeDiary(Long petPk, DiaryDTO.DiaryRequest addDiaryRequest) throws RuntimeException {

        Pet findPet = petRepository.findById(petPk)
                .orElseThrow(() -> new NotFoundException("반려동물 정보가 없습니다"));

        Diary diary = Diary.builder()
                .pet(findPet)
                .mealCnt(addDiaryRequest.getMealCnt())
                .snackCnt(addDiaryRequest.getSnackCnt())
                .walkCnt(addDiaryRequest.getWalkCnt())
                .conditionOfDefecation(addDiaryRequest.getConditionOfDefecation())
                .defecationText(addDiaryRequest.getDefecationText())
                .etc(addDiaryRequest.getEtc())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .calendarDate(addDiaryRequest.getCalendarDate())
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

    public DiaryDTO.DiaryListResponse getDiaryList(DiaryDTO.DiaryListRequest request){
        DiaryDTO.DiaryListResponse response =
                DiaryDTO.DiaryListResponse.builder()
                        .diaryPk(request.getDiaryPk())
                        .calendarDate(request.getCalendarDate())
                        .build();
        return response;
    }
    public List<DiaryDTO.DiaryListResponse> getDiaryList(Long petPk) throws RuntimeException {
        Pet findPet = petRepository.findById(petPk)
                .orElseThrow(() -> new NotFoundException("반려동물 정보가 없습니다"));
        List<Diary> findDiary = diaryRepository.findByPet(findPet);
        List<DiaryDTO.DiaryListResponse> diaryDTOs = new ArrayList<>();
        for(Diary diary: findDiary ){
            DiaryDTO.DiaryListRequest request =
                    DiaryDTO.DiaryListRequest.diarys(
                            diary.getPk(),
                            diary.getCalendarDate()
                    );
            DiaryDTO.DiaryListResponse response = getDiaryList(request);
            DiaryDTO.DiaryListResponse diaryListResponse =
                    DiaryDTO.DiaryListResponse.builder()
                            .diaryPk(response.getDiaryPk())
                            .calendarDate(response.getCalendarDate())
                            .build();
            diaryDTOs.add(diaryListResponse);

        }
        return  diaryDTOs;


    }

    public DiaryDTO.DiaryResponse getDiary(Long diaryPk) throws RuntimeException {

        Diary diary = diaryRepository.findById(diaryPk)
                .orElseThrow(() -> new NotFoundException("다이어리 정보가 없습니다."));

        List<DiaryMedicine> diaryMedicines = diaryMedicineRepository.findByDiary(diary);

        List<MedicineDTO.RequestMedicine> requestMedicines = diaryMedicines.stream()
                .map(diaryMedicine -> new MedicineDTO.RequestMedicine(
                        diaryMedicine.getMedicine().getName(),
                        Math.toIntExact(diaryMedicine.getMedicineCnt())))
                .collect(Collectors.toList());

        MedicineDTO.ResponseMedicineList responseMedicineList = MedicineDTO.ResponseMedicineList.builder()
                .cnt(requestMedicines.size())
                .list(requestMedicines)
                .build();

        return new DiaryDTO.DiaryResponse(
                diary.getMealCnt(),
                diary.getSnackCnt(),
                diary.getWalkCnt(),
                diary.getConditionOfDefecation(),
                diary.getDefecationText(),
                diary.getEtc(),
                responseMedicineList,
                diary.formatDate(diary.getCalendarDate())
        );
    }

    public void delete(Long diaryPk) throws RuntimeException {
        Diary diary = diaryRepository.findById(diaryPk)
                .orElseThrow(() -> new NotFoundException("다이어리 정보가 없습니다."));

        List<DiaryMedicine> diaryMedicines = diaryMedicineRepository.findByDiary(diary);
        diaryMedicineRepository.deleteAll(diaryMedicines);

        diaryRepository.delete(diary);

    }

    public DiaryDTO.DiaryResponse updateDiary(Long diaryPk, DiaryDTO.DiaryUpdateRequest diaryRequest) throws RuntimeException {

        Diary diary = diaryRepository.findById(diaryPk)
                .orElseThrow(() -> new NotFoundException("다이어리 정보가 없습니다."));

        diary.updateInfo(diaryRequest.getMealCnt(), diary.getSnackCnt(), diary.getWalkCnt()
                , diary.getConditionOfDefecation(), diaryRequest.getDefecationText(), diaryRequest.getEtc());

        // 기존 DiaryMedicine 정보 업데이트
        List<DiaryMedicine> existingDiaryMedicines = diaryMedicineRepository.findByDiary(diary);
        for (DiaryMedicine existingDiaryMedicine : existingDiaryMedicines) {
            boolean found = false;
            for (MedicineDTO.RequestMedicine requestMedicine : diaryRequest.getMedicineList()) {
                if (existingDiaryMedicine.getMedicine().getName().equals(requestMedicine.getName())) {
                    existingDiaryMedicine.setMedicineCnt(requestMedicine.getCnt());
                    diaryMedicineRepository.save(existingDiaryMedicine);
                    found = true;
                    break;
                }
            }
            if (!found) {
                diaryMedicineRepository.delete(existingDiaryMedicine);
            }
        }

        // 새로운 약 정보 추가
        for (MedicineDTO.RequestMedicine requestMedicine : diaryRequest.getMedicineList()) {
            if (existingDiaryMedicines.stream().noneMatch(dm -> dm.getMedicine().getName().equals(requestMedicine.getName()))) {
                Medicine medicine = medicineRepository.findByName(requestMedicine.getName())
                        .orElseThrow(() -> new NotFoundException("약 정보가 없습니다: " + requestMedicine.getName()));

                DiaryMedicine newDiaryMedicine = DiaryMedicine.builder()
                        .diary(diary)
                        .medicine(medicine)
                        .medicineCnt(requestMedicine.getCnt())
                        .build();

                diaryMedicineRepository.save(newDiaryMedicine);
            }
        }

        diaryRepository.save(diary);

        // 다이어리 약 정보 조회
        List<DiaryMedicine> updatedDiaryMedicines = diaryMedicineRepository.findByDiary(diary);
        List<MedicineDTO.RequestMedicine> responseMedicines = updatedDiaryMedicines.stream()
                .map(diaryMedicine -> new MedicineDTO.RequestMedicine(
                        diaryMedicine.getMedicine().getName(),
                        diaryMedicine.getMedicineCnt()))
                .collect(Collectors.toList());

        MedicineDTO.ResponseMedicineList responseMedicineList = MedicineDTO.ResponseMedicineList.builder()
                .cnt(responseMedicines.size())
                .list(responseMedicines)
                .build();

        return new DiaryDTO.DiaryResponse(
                diary.getMealCnt(),
                diary.getSnackCnt(),
                diary.getWalkCnt(),
                diary.getConditionOfDefecation(),
                diary.getDefecationText(),
                diary.getEtc(),
                responseMedicineList,
                diary.formatDate(diary.getCalendarDate())
        );
    }


}

