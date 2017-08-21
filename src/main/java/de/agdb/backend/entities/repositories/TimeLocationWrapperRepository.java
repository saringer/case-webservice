package de.agdb.backend.entities.repositories;

import de.agdb.backend.entities.Users;
import de.agdb.backend.entities.schedule_wrapper_objects.TimeLocationWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeLocationWrapperRepository extends JpaRepository<TimeLocationWrapper, Long> {
    TimeLocationWrapper save(TimeLocationWrapper timeLocationWrapper);
}
