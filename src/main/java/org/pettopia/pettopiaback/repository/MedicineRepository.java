package org.pettopia.pettopiaback.repository;

import org.pettopia.pettopiaback.domain.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByPetPk(Long petPk);

}
