package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.*;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AppUserSearchService;
import SoloProject.SocialMediaApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final AppUserSearchService appUserSearchService;
    private final AppUserRepository appUserRepository;

    private final PostService postService;

    @Autowired
    public UserController(AppUserSearchService appUserSearchService, AppUserRepository appUserRepository, PostService postService) {
        this.appUserSearchService = appUserSearchService;
        this.appUserRepository = appUserRepository;
        this.postService = postService;
    }

    @GetMapping
    public String helloWorld() {
        return "Hello, user!";
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<AppUser> findUserbyId(@PathVariable Long userId) {
        return appUserSearchService.findByAppUserID(userId);
    }

//    @GetMapping("/user/{username}")
//    public ResponseEntity<AppUser> findByUsername(@PathVariable String username) {
//        return appUserService.findUser(username);
//    }

    @GetMapping("/user/getAllWebsiteUsers")
    public ResponseEntity<List<AppUser>> GetAllWebsiteUsers() {
        return appUserSearchService.getAllUsers();
    }

    @PutMapping("/user/block/{userId}/{blockId}")
    public ResponseEntity<AppUser> BlockUser(@PathVariable Long userId, @PathVariable Long blockId) {
        return appUserSearchService.blockUser(userId, blockId);
    }

    @GetMapping("user/{userid}/{profileUserId}")
    public ResponseEntity<List<Post>> displayAllUserPosts(@PathVariable Long userid, @PathVariable Long profileUserId) {
        List<Post> posts = postService.getPostsByPosterId(userid, profileUserId);
        if (posts == null || posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
    }
}