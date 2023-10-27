package SoloProject.SocialMediaApp.service;
import org.springframework.http.ResponseEntity;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSenderService {

    private Properties properties;
    public ResponseEntity<?> sendEmail(String userEmail) {
        String to = userEmail;
        String from = "nateapplications@gmail.com";
        String password = "wwjx fxad xcfn xtww";

        String host = "localhost";
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Recovery Password");

            String recoveryPassword = "your_recovery_password";
            message.setText("This is your recovery password: " + recoveryPassword);

            Transport.send(message);
            System.out.println("Sent Message Successfully");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
        return ResponseEntity.ok("Email sent successfully to " + userEmail);
    }
}