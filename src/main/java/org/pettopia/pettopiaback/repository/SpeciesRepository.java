package org.pettopia.pettopiaback.repository;

import org.pettopia.pettopiaback.domain.Pet;
import org.pettopia.pettopiaback.domain.Species;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeciesRepository extends JpaRepository<Species, Long> {
}
