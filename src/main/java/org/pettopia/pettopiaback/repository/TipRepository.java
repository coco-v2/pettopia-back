package org.pettopia.pettopiaback.repository;

import org.pettopia.pettopiaback.domain.PetType;
import org.pettopia.pettopiaback.domain.Tip;
import org.pettopia.pettopiaback.domain.TipCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipRepository extends JpaRepository<Tip, Long> {
    List<Tip> findByPetTypeAndTipCategory(PetType petType, TipCategory tipCategory);
}
