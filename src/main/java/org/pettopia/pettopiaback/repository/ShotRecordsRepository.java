package org.pettopia.pettopiaback.repository;
import org.pettopia.pettopiaback.domain.Pet;
import org.pettopia.pettopiaback.domain.ShotRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShotRecordsRepository  extends JpaRepository<ShotRecords,Long>{

    List<ShotRecords> findByPet(Pet pet);
}
