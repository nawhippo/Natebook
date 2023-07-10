package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @Query("SELECT u from User u WHERE u.username = :username")
    User findByUsername(String username);
}