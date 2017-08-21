package de.agdb.backend.entities.repositories;

import de.agdb.backend.entities.Categories;
import de.agdb.backend.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

/*interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {
    T findOne(ID id);
    T save(T entity);
}*/

public interface UsersRepository extends JpaRepository<Users, Long>{

    List<Users> findByUsername(String username);
    Users save(Users user);

    @Query(value = "select * from  u where cast(letters as char) like :letters%", nativeQuery = true)
    public List<Categories> findCategoriesBeginningWith(@Param("letters") String letters);


    //Lis<Registration> findByPlaceIgnoreCaseContaining(String place);

    //List<Users> findByPrice(long price);
    //List<Users> findByNameAndAuthor(String name, String author);
    /*
    Customer customerToUpdate = customerRepository.getOne(id);
    customerToUpdate.setName(customerDto.getName);
    customerRepository.save(customerToUpdate);
     */
}
