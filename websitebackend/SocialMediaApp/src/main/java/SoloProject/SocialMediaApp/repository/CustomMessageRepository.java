package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.CustomMessage;
import SoloProject.SocialMediaApp.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CustomMessageRepository extends JpaRepository<CustomMessage, Long> {

    List<CustomMessage> findAll();
}