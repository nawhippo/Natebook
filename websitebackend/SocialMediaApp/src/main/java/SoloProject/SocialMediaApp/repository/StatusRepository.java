package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    List<Status> findByDeathBefore(LocalDateTime dateTime);
    Status findByAppUserID(Long appUserID);
    void deleteByAppUserID(Long appUserID);
}