package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.CustomMessage;
import SoloProject.SocialMediaApp.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomMessageRepository extends JpaRepository<CustomMessage, Long> {
    List<CustomMessage> findByRecipientsContains(Long recipientId);
    List<CustomMessage> findBySender(Long senderId);

    List<CustomMessage> findByGroupChatId(Long groupChatId);
}