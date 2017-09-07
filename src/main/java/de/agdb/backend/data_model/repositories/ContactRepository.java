package de.agdb.backend.data_model.repositories;

import de.agdb.backend.data_model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Contact save(Contact contact);

}
