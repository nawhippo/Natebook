package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.service.AppUserService;
import SoloProject.SocialMediaApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api") // The base path for all endpoints in this controller
public class AddressBookController {

    private final AppUserService userService;
    private final PostService postService;
    @Autowired
    public AddressBookController(AppUserService userservice, PostService postservice) {
        this.userService = userservice;
        this.postService = postservice;
    }

    @GetMapping("/home")
    public ResponseEntity<List<AppUser>> getHomePageData() {
        return userService.getAllUsers();
    }


    @GetMapping("/publicFeed")
    public ResponseEntity<List<Post>> getAllPublicPosts() {
        List<Post> publicPosts = postService.getAllPublicPosts();

        if (publicPosts != null && !publicPosts.isEmpty()) {
            return new ResponseEntity<>(publicPosts, HttpStatus.OK);
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