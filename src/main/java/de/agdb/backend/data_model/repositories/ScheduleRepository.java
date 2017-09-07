package de.agdb.backend.data_model.repositories;

import de.agdb.backend.data_model.schedule_wrapper_objects.ScheduleWrapper;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScheduleRepository extends JpaRepository<ScheduleWrapper, Long> {
    //List<Users> findByUsername(String username);
    ScheduleWrapper save(ScheduleWrapper schedule);
    // List<Registration> findByPlaceIgnoreCaseContaining(String place);
    //List<Users> findByPrice(long price);
    //List<Users> findByNameAndAuthor(String name, String author);
    /*
    Customer customerToUpdate = customerRepository.getOne(id);
customerToUpdate.setName(customerDto.getName);
customerRepository.save(customerToUpdate);
     */
}

