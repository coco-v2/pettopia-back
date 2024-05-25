package org.pettopia.pettopiaback.repository;

import org.pettopia.pettopiaback.domain.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetTypeRepository extends JpaRepository<PetType, Long> {
}
