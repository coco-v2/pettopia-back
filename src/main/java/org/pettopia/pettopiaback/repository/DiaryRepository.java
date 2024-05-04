package org.pettopia.pettopiaback.repository;

import org.pettopia.pettopiaback.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

}
