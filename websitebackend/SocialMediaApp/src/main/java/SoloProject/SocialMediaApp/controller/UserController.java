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
        this.appUserService = appUserService;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping
    public String helloWorld() {
        return "Hello, user!";
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<AppUser> findUserbyId(@PathVariable Long userId) {
        return appUserService.findByAppUserID(userId);
    }

//    @GetMapping("/user/{username}")
//    public ResponseEntity<AppUser> findByUsername(@PathVariable String username) {
//        return appUserService.findUser(username);
//    }

    @GetMapping("/user/getAllWebsiteUsers")
    public ResponseEntity<List<AppUser>> GetAllWebsiteUsers() {
        return appUserService.getAllUsers();
    }

    @PutMapping("/user/{userId}/{blockId}")
    public ResponseEntity<AppUser> BlockUser(@PathVariable Long userId, @PathVariable Long blockId) {
        return appUserService.blockUser(userId, blockId);
    }

    @GetMapping("user/{userid}/{profileUserId}")
    public ResponseEntity<List<Post>> displayAllUserPosts(@PathVariable Long userid, @PathVariable Long profileUserId){
        return appUserService.getAllUserPosts(userid, profileUserId);
    }
}