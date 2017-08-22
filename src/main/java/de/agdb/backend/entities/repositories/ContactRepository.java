package de.agdb.backend.entities.repositories;

import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.schedule_wrapper_objects.ScheduleWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Contact save(Contact contact);

}
