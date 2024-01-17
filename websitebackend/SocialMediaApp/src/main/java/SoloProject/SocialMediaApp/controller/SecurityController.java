package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.JwtUtil;
import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.AppUserDTO;
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

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class SecurityController {

    private final AppUserRepository appUserRepository;
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Autowired
    public SecurityController(AppUserRepository appUserRepository, AccountService accountService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) throws Exception {
        this.appUserRepository = appUserRepository;
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> handleLogin(@RequestBody LoginRequest loginRequest) {
//        System.out.println("Login attempt for user: " + loginRequest.getUsername());
//
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.getUsername(),
//                            loginRequest.getPassword()
//                    )
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            System.out.println("Authentication successful for user: " + loginRequest.getUsername());
//
//            AppUser authenticatedUser = appUserRepository.findByUsername(loginRequest.getUsername());
//            if (authenticatedUser != null) {
//                authenticatedUser.setOnline(true);
//                System.out.println("Authenticated user set to online: " + authenticatedUser.getUsername());
//
//
//                String jwt = jwtUtil.generateToken(authenticatedUser);
//
//                Map<String, Object> response = new HashMap<>();
//                AppUserDTO authenticatedUserDTO = new AppUserDTO(authenticatedUser);
//                response.put("user", authenticatedUserDTO);
//                response.put("jwt", jwt);
//
//                return ResponseEntity.ok(response);
//            } else {
//                // Handle case where user is not found in the repository
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("{\"message\": \"User not found\"}");
//            }
//
//        } catch (AuthenticationException e) {
//            System.out.println("Authentication failed for user: " + loginRequest.getUsername() + " - " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("{\"message\": \"Invalid username or password\"}");
//        }
//    }
    @PostMapping("/{userid}/logout")
    public ResponseEntity<?> handleLogout(@PathVariable long userid) {
        AppUser user = appUserRepository.findByAppUserID(userid);
        user.setOnline(false);
        appUserRepository.save(user);
        return ResponseEntity.ok(user);
   }



    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

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