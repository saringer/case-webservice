package de.agdb.backend.entities.repositories;

import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Contact;
import de.agdb.backend.entities.schedule_wrapper_objects.CategoriesWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriesWrapperRepository extends JpaRepository<CategoriesWrapper, Long> {
    CategoriesWrapper save(CategoriesWrapper categoriesWrapper);
    List<CategoriesWrapper> findById(Long id);
}
