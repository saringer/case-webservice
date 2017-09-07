package de.agdb.backend.data_model.repositories;

import de.agdb.backend.data_model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    Categories save(Categories categories);
    List<Categories> findByTitle(String title);


}
