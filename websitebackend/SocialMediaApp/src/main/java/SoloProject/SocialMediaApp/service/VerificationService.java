package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.EmailToken;
import SoloProject.SocialMediaApp.repository.EmailTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationService {
    @Autowired
    private EmailTokenRepository emailTokenRepository;

    @Autowired
    private MailService mailService;

    @Value("${app.verification-url}")
    private String verificationUrl;

    public void createAndSendEmailToken(String email) {
        String token = UUID.randomUUID().toString().substring(0, 8);
        EmailToken emailToken = new EmailToken();
        emailToken.setToken(token);
        emailToken.setEmail(email);
        emailToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        emailTokenRepository.save(emailToken);
        String content = "Your email verification code is: " + token + ". Please enter this code on the verification page to verify your email.";
        mailService.sendVerificationMail(email, "Verify your email", content);
    }


    public boolean verifyEmailToken(String token) {
        EmailToken emailToken = emailTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid verification token."));

        if (emailToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification token is expired.");
        }
        emailToken.setVerified(true);
        emailTokenRepository.save(emailToken);
        return true;
    }

    public boolean getVerified(String email){
        Optional<EmailToken> token = emailTokenRepository.findByEmail(email);
        return token.get().isVerified();
    }

}