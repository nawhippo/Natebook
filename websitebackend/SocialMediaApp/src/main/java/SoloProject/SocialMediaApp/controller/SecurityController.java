package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        user.setOnline(true);
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

    @PostMapping("/{userid}/logout")
    public ResponseEntity<?> handleLogout(@PathVariable long userid) {
        AppUser user = appUserRepository.findByAppUserID(userid);
        user.setOnline(false);
        return ResponseEntity.ok(user);
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