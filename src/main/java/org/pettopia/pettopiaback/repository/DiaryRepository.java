package org.pettopia.pettopiaback.repository;

import org.pettopia.pettopiaback.domain.ConditionOfDefecation;
import org.pettopia.pettopiaback.domain.Diary;
import org.pettopia.pettopiaback.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary>findByPet(Pet pet);
    List<Diary>findByPetAndConditionOfDefecation(Pet pet, ConditionOfDefecation conditionOfDefecation);
    Diary findByPetAndCalendarDate(Pet pet, LocalDate calendarDate);
}
