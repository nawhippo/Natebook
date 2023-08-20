package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.*;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    private final AppUserServiceImpl userserviceimpl;
    private final AppUserRepository appUserRepository;

    @Autowired
    public UserController(AppUserServiceImpl userserviceimpl, AppUserRepository appUserRepository) {
        this.userserviceimpl = userserviceimpl;
        this.appUserRepository = appUserRepository;
    }
    @GetMapping
    public String helloWorld() {
        return "Hello, user!";
    }

    @GetMapping("/user")
    public ResponseEntity<AppUser> getUserData() {
        Long userId = 1L;
        AppUser user = appUserRepository.findById(userId).orElse(null);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<AppUser> findUserbyId(@PathVariable Long userId) {
        return userserviceimpl.findByAppUserID(userId);
    }

    @GetMapping("/getAllWebsiteUsers")
    public ResponseEntity<List<AppUser>> GetAllWebsiteUsers() {
        return userserviceimpl.getAllUsers();
    }
}