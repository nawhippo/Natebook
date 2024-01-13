package SoloProject.SocialMediaApp.repository;
import SoloProject.SocialMediaApp.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
