package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.Status;
import org.joda.time.DateTime;  // Correct import statement
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    List<Status> findByDeathBefore(DateTime dateTime);
}