package de.agdb.backend.entities.repositories;

import de.agdb.backend.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    Categories save(Categories categories);
    List<Categories> findByTitle(String title);
}
