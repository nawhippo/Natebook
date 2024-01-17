package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AccountService;
import SoloProject.SocialMediaApp.service.AppUserSearchService;
import SoloProject.SocialMediaApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api") // The base path for all endpoints in this controller
public class FeedController {

    private final AppUserSearchService userService;
    private final PostService postService;
    private final AppUserRepository appUserRepository;

    private final AccountService accountService;
    @Autowired
    public FeedController(AppUserSearchService userservice, PostService postservice, AppUserRepository appUserRepository, AccountService accountService) {
        this.userService = userservice;
        this.postService = postservice;
        this.appUserRepository = appUserRepository;
        this.accountService = accountService;
    }

    @GetMapping("/home")
    public ResponseEntity<List<AppUser>> getHomePageData() {
        return userService.getAllUsers();
    }


    @GetMapping("/publicFeed")
    public ResponseEntity<?> getAllPublicPosts() {
        List<Post> publicPosts = postService.getAllPublicPosts();

        if (publicPosts != null && !publicPosts.isEmpty()) {
            return new ResponseEntity<>(publicPosts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No public posts found", HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{userId}/friendPosts")
    public ResponseEntity<?> getAllFriendPosts(
            @PathVariable Long userId,
            Authentication authentication) {

        AppUser appUser = appUserRepository.findByAppUserID(userId);
        if (!accountService.checkAuthenticationMatch(appUser.getUsername(), authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        List<Post> posts = postService.getAllFriendPosts(userId);
        if (posts != null && !posts.isEmpty()) {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/about") // The endpoint URL: /api/about
    public String about() {
        return "{\"message\": \"Welcome to my about page, it is a react/java application. It is a simple CRUD application, " +
                "where users are able to post, comment, friend, and message each other.\"}";
    }
}