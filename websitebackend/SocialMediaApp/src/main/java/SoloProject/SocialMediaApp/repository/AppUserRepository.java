package SoloProject.SocialMediaApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import SoloProject.SocialMediaApp.models.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    List<AppUser> findByFirstname(String firstName);

    List<AppUser> findByLastname(String lastName);
    AppUser findByUsername(String username);

    AppUser findByAppUserID(long uid);

    @Query("SELECT u FROM AppUser u WHERE u.firstname = :firstname AND u.lastname = :lastname")
    List<AppUser> findByFirstnameAndLastname(@Param("firstname") String firstname, @Param("lastname") String lastname);
}