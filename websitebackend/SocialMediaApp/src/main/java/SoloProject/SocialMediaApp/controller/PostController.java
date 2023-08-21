package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.Comment;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
    private final AppUserServiceImpl userserviceimpl;
    private final AppUserRepository appUserRepository;

    @Autowired
    public PostController(AppUserServiceImpl userserviceimpl, AppUserRepository appUserRepository) {
        this.userserviceimpl = userserviceimpl;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/post/{userId}/{postId}")
    public ResponseEntity<Post> getPostById(
            @PathVariable Long userId,
            @PathVariable Long postId) {
        return userserviceimpl.getPostById(userId, postId);
    }

    @PostMapping("/post/{userId}/{postId}/createComment")
    public ResponseEntity<?> createComment(
            @PathVariable Long userId,
            @PathVariable Long postId,
            @RequestBody Comment comment
    ) {
        return userserviceimpl.createComment(userId, postId, comment);
    }

    @PostMapping("/post/{userId}/createPost")
    public ResponseEntity<Post> createPost(
            @PathVariable Long userId,
            @RequestBody Post post
    ) {
        return userserviceimpl.createPost(userId, post);
    }

    @GetMapping("/post/{userId}/posts/{targetUserId}")
    public ResponseEntity<List<Post>> getAllPostsByUserId(@PathVariable Long userId, @PathVariable Long targetUserId) {
        return userserviceimpl.getPostsByUserId(userId, targetUserId);
    }

    @GetMapping("/post/{userId}/postsByUsername/{friendUsername}")
    public ResponseEntity<List<Post>> getAllPostsByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return userserviceimpl.getPostsByUsername(userId, friendUsername);
    }
}
