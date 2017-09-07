package de.agdb.backend.data_model.repositories;

import de.agdb.backend.data_model.schedule_wrapper_objects.TimeLocationWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeLocationWrapperRepository extends JpaRepository<TimeLocationWrapper, Long> {
    TimeLocationWrapper save(TimeLocationWrapper timeLocationWrapper);
}
