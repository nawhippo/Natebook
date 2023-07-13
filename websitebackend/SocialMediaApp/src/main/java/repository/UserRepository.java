package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByFirstname(String firstName);
    List<User> findByLastname(String lastName);
    List<User> FindByFirstNameAndLastName(String firstName, String lastName);
}