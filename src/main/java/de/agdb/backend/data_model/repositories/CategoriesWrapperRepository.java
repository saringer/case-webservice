package de.agdb.backend.data_model.repositories;

import de.agdb.backend.data_model.schedule_wrapper_objects.CategoriesWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriesWrapperRepository extends JpaRepository<CategoriesWrapper, Long> {
    CategoriesWrapper save(CategoriesWrapper categoriesWrapper);
    List<CategoriesWrapper> findById(Long id);
}
