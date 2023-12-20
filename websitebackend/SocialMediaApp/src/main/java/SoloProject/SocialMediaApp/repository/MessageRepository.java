package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderid(Long sender);
}