package org.pettopia.pettopiaback.repository;

import org.pettopia.pettopiaback.domain.Diary;
import org.pettopia.pettopiaback.domain.DiaryMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryMedicineRepository extends JpaRepository<DiaryMedicine, Long> {
    List<DiaryMedicine> findByDiary(Diary diary);

    void deleteByDiary(Diary diary);
}