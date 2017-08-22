package de.agdb.backend.entities.repositories;

import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    Categories save(Categories categories);
    List<Categories> findByTitle(String title);


}
