package de.agdb.backend.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;

/*interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {
    T findOne(ID id);
    T save(T entity);
}*/

public interface UsersRepository extends JpaRepository<Users, Long>{

    List<Users> findByUsername(String username);
    Users save(Users user);

    //List<Users> findByPrice(long price);
    //List<Users> findByNameAndAuthor(String name, String author);
    /*
    Customer customerToUpdate = customerRepository.getOne(id);
    customerToUpdate.setName(customerDto.getName);
    customerRepository.save(customerToUpdate);
     */
}
