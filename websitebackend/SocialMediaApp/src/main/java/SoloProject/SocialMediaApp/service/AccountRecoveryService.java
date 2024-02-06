package SoloProject.SocialMediaApp.service;


import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Properties;
import java.util.Random;

@Service
public class AccountRecoveryService {

    private Properties properties;


    @Autowired
    private MailService mailService;

    @Autowired
    AppUserRepository appUserRepository;

    public ResponseEntity<?> sendEmail(String userEmail) {
        try {
            String recoveryPassword = generateOTP();
            AppUser appUser = appUserRepository.findByEmail(userEmail);

            if (appUser != null) {
                appUser.setOtp(recoveryPassword);
                appUserRepository.save(appUser);
            } else {
                return ResponseEntity.badRequest().body("User email not found.");
            }
            String subject = "Recovery Password";
            String content = "This is your recovery password: " + recoveryPassword;

            mailService.sendVerificationMail(userEmail, subject, content);
            System.out.println("Sent Message Successfully");
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError().body("Failed to send email to " + userEmail);
        }
        return ResponseEntity.ok("Email sent successfully to " + userEmail);
    }

    //generate 6 digit otp
    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return Integer.toString(otp);
    }
}