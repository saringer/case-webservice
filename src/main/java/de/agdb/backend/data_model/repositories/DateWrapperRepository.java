package de.agdb.backend.data_model.repositories;

import de.agdb.backend.data_model.schedule_wrapper_objects.DateWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateWrapperRepository extends JpaRepository<DateWrapper,Long> {
}
