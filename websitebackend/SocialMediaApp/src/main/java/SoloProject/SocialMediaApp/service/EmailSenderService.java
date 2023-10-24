package SoloProject.SocialMediaApp.service;
import org.springframework.http.ResponseEntity;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSenderService {
    public ResponseEntity<?> sendEmail(String userEmail) {
        String to = userEmail;
        //how to actually authorize email with this, gmail method again potentially.
        String from = "";
        String host = "localhost";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Recovery Password");

            String recoveryPassword = "your_recovery_password"; // Define the recovery password
            message.setText("This is your recovery password: " + recoveryPassword);

            Transport.send(message);
            System.out.println("Sent Message Successfully");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

        // You might want to return a response entity based on the success or failure of the email sending
        return ResponseEntity.ok("Email sent successfully to " + userEmail);
    }
}