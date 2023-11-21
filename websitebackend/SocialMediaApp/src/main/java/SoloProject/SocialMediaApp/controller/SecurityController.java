package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

@Controller
@RequestMapping("/api") // The base path for all endpoints in this controller
public class SecurityController {

    private AppUserRepository appUserRepository;

    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthenticationManager authenticationManager;
    public SecurityController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Fetch the authenticated user details and return the user object
            AppUser authenticatedUser = (AppUser) authentication.getPrincipal();
            authenticatedUser.setOnline(true);
            return ResponseEntity.ok(authenticatedUser);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\": \"Invalid username or password\"}");
        }
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