package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api") // The base path for all endpoints in this controller
public class SecurityController {

    private AppUserRepository appUserRepository;

    public SecurityController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        // Perform login logic here, validate credentials, etc.
        // If login fails, return an error message or redirect to the login page with an error message

        AppUser user = appUserRepository.findByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                // Return the user object as the response entity
                return ResponseEntity.ok(user);
            } else {
                // If login fails, return an error message
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid username or password\"}");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid username or password\"}");
    }
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    // Create a data class to map the JSON request body to Java objects
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return this.username;
        }
        public String getPassword() {
            return this.password;
        }
    }
}