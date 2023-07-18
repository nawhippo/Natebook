package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByFirstname(String firstName);
    List<User> findByLastname(String lastName);
    User findByUserID(long uid);
    @Query("SELECT u FROM User u WHERE u.firstname = :firstname AND u.lastname = :lastname")
    List<User> findByFirstnameAndLastname(@Param("firstname") String firstname, @Param("lastname") String lastname);
}