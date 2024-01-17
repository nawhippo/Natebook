package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderid(Long senderId);
    List<Message> findByThreadid(Long threadId);

    @Query("SELECT m FROM Message m WHERE m.threadid = :threadId AND m.timestamp > :lastChecked")
    List<Message> findNewMessages(@Param("threadId") Long threadId, @Param("lastChecked") LocalDateTime lastChecked);
}