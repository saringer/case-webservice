package de.agdb.backend.data_model.repositories;

import de.agdb.backend.data_model.DailyEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyEventRepository extends JpaRepository<DailyEvent, Long> {

    List<DailyEvent> findByDateWrapperId(Long dateWrapperID);
}
