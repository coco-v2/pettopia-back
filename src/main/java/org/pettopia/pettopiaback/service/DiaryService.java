package org.pettopia.pettopiaback.service;


import org.pettopia.pettopiaback.dto.DiaryDTO;
import org.pettopia.pettopiaback.domain.Diary;
import org.pettopia.pettopiaback.domain.Pet;
import org.pettopia.pettopiaback.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.pettopia.pettopiaback.repository.DiaryRepository;
import org.pettopia.pettopiaback.repository.PetRepository;



@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

    private final PetRepository petRepository;
    private final DiaryRepository diaryRepository;

    public Diary makeDiary(DiaryDTO.AddDiaryRequest addDiaryRequest) throws RuntimeException{

        Pet findPet = petRepository.findById(addDiaryRequest.getPetPk())
      .orElseThrow(() -> new NotFoundException("반려동물 정보가 없습니다"));

        Diary diary = Diary.makeDiary(findPet,addDiaryRequest.getMealCont(), addDiaryRequest.getSnackCnt(), addDiaryRequest.getWalkCnt(), addDiaryRequest.getConditionOfDefecation(), addDiaryRequest.getDefecationText());
        return diaryRepository.save(diary);


    }
}
