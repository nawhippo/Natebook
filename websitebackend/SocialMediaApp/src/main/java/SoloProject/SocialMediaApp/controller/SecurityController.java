package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Controller
@RequestMapping("/api") // The base path for all endpoints in this controller
public class SecurityController {

    private AppUserRepository appUserRepository;
    private AccountService accountService;

    public SecurityController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        AppUser user = appUserRepository.findByUsername(username);
        if (user != null) {
            if(user.isGoogleUser){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid username or password\"}");
            }
            if (user.getPassword().equals(password)) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid username or password\"}");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid username or password\"}");
    }

    @PostMapping("/loginwithGoogle")
    public ResponseEntity<?> handleGoogleLogin(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {
        // Create username from email
        String username = email.split("@")[0];

        AppUser existingUser = appUserRepository.findByUsername(username);
        if (existingUser != null) {
            // User already exists, update if needed
            return ResponseEntity.ok(existingUser);
        } else {
            // Create a new user account with the provided information
            // You may need to adjust the logic in accountService.createAccountFromUser
            // to handle the provided user information.
            return accountService.createAccountFromGoogle(firstName, lastName, email);
        }
    }


    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    //create a data class to map the JSON request body to Java objects
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