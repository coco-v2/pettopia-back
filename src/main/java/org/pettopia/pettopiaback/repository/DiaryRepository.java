package org.pettopia.pettopiaback.repository;

import org.pettopia.pettopiaback.domain.Diary;
import org.pettopia.pettopiaback.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
List<Diary>findByPet(Pet pet);
}
