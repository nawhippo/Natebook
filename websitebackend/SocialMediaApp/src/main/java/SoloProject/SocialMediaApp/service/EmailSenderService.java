package SoloProject.SocialMediaApp.service;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.Random;

@Service
public class EmailSenderService {

    private Properties properties;

    public ResponseEntity<?> sendEmail(String userEmail) {
        String to = userEmail;
        String from = "...";
        String password = "...";

        String host = "smtp.gmail.com"; // Use Gmail's SMTP server
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Recovery Password");

            String recoveryPassword = generateOTP();
            message.setText("This is your recovery password: " + recoveryPassword);

            Transport.send(message);
            System.out.println("Sent Message Successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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