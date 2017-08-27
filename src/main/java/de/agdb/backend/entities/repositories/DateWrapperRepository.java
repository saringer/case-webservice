package de.agdb.backend.entities.repositories;

import de.agdb.backend.entities.schedule_wrapper_objects.DateWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateWrapperRepository extends JpaRepository<DateWrapper,Long> {
}
