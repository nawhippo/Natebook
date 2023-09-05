package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.Comment;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    private final AppUserRepository appUserRepository;

    @Autowired
    public PostController(PostService postService, AppUserRepository appUserRepository) {
        this.postService = postService;
        this.appUserRepository = appUserRepository;
    }


    @PostMapping("/post/{userId}/createPost")
    public ResponseEntity<Post> createPost(
            @PathVariable Long userId,
            @RequestBody Post post
    ) {
        return postService.createPost(userId, post);
    }


    @GetMapping("/post/{userId}/{posterId}/{postId}")
    public ResponseEntity<Post> getPostById(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId) {
        return postService.getPostById(userId, posterId, postId);
    }

    @PutMapping("/post/{userId}/{posterId}/{postId}/updateReactionPost")
    public ResponseEntity<?> updateReaction(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @RequestBody Map<String, String> payload
    ) {
        String action = payload.get("action");
        return postService.handleReaction(userId, posterId, postId, null, action);
    }

    @PostMapping("/post/{userId}/{postId}/createComment")
    public ResponseEntity<?> createComment(
            @PathVariable Long userId,
            @PathVariable Long postId,
            @RequestBody Comment comment
    ) {
        return postService.createComment(userId, postId, comment);
    }

    @PutMapping("/post/{userId}/{posterId}/{postId}/{commentId}/updateReactionComment")
    public ResponseEntity<?> updateCommentReaction(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody Map<String, String> payload
    ) {
        String action = payload.get("action");
        return postService.handleReaction(userId, posterId, postId, commentId, action);
    }






    @GetMapping("/post/{userId}/friendPosts")
    public ResponseEntity<List<Post>> getAllFriendPosts(@PathVariable Long userId) {
        return postService.getAllFriendPosts(userId);
    }


    @GetMapping("/post/{userId}/posts/{targetUserId}")
    public ResponseEntity<List<Post>> getAllPostsByUserId(@PathVariable Long userId, @PathVariable Long targetUserId) {
        return postService.getPostsByUserId(userId, targetUserId);
    }


    @GetMapping("/post/{userId}/postsByUsername/{friendUsername}")
    public ResponseEntity<List<Post>> getAllPostsByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return postService.getPostsByUsername(userId, friendUsername);
    }

    @DeleteMapping("/post/{userId}/{postId}/deletePost")
        public ResponseEntity<Post> deletePost(@PathVariable Long userId, @PathVariable Long postId){
            return postService.deletePost(userId, postId);
        }


    @DeleteMapping("/post/{userId}/{postId}/{commentId}/deleteComment")
    public ResponseEntity<Post> deleteComment(@PathVariable Long userId, @PathVariable Long postId, @PathVariable Long commentId){
        return postService.deleteComment(userId, postId, commentId);
    }
    }

