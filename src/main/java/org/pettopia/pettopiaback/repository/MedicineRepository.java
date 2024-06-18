package org.pettopia.pettopiaback.repository;

import org.pettopia.pettopiaback.domain.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByPetPk(Long petPk);

    Optional<Medicine> findByName(String name);

}
