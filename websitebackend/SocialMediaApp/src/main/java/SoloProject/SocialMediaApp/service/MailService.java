package SoloProject.SocialMediaApp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    public void sendVerificationMail(String toEmail, String subject, String content) {
        // Log the email and content
        log.info("Sending email to: {}", toEmail);
        log.info("Email content: {}", content);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
        } catch (Exception e) {
            // Log the exception which might contain the illegal character info
            log.error("Error sending email", e);
            // Optionally, rethrow the exception or handle it based on your application's needs
        }
    }
}