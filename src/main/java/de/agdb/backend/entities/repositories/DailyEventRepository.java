package de.agdb.backend.entities.repositories;

import de.agdb.backend.entities.DailyEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyEventRepository extends JpaRepository<DailyEvent, Long> {

    List<DailyEvent> findByDateWrapperId(Long dateWrapperID);
}
