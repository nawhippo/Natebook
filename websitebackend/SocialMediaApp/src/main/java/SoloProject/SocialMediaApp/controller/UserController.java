package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.*;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AppUserService;
import SoloProject.SocialMediaApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;

    @Autowired
    public UserController(AppUserService appUserService, AppUserRepository appUserRepository) {
        this.appUserService= appUserService;
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
        return appUserService.findByAppUserID(userId);
    }
@GetMapping("/getAllWebsiteUsers") public ResponseEntity<List<AppUser>> GetAllWebsiteUsers() { return appUserService.getAllUsers(); } }