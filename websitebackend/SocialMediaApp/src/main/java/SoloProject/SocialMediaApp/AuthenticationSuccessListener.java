package SoloProject.SocialMediaApp;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // You can customize the logging statement here
        System.out.println("User authenticated successfully: " + event.getAuthentication().getName());
    }
}