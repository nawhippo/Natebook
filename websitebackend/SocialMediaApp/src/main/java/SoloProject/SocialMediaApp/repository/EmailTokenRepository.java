package SoloProject.SocialMediaApp.repository;
import SoloProject.SocialMediaApp.models.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
    Optional<EmailToken> findByToken(String token);
    Optional<EmailToken> findByEmail(String token);
}