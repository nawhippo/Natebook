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
    public ResponseEntity<Post> updatePostReaction(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @RequestBody Map<String, String> payload
    ) {
        String action = payload.get("action");

        if ("Like".equals(action)) {
            return postService.addLikePost(userId, posterId, postId);
        } else if ("Dislike".equals(action)) {
            return postService.addDislikePost(userId, posterId, postId);
        } else if ("Unlike".equals(action)) {
            return postService.removeLikePost(userId, posterId, postId);
        } else if ("Undislike".equals(action)) {
            return postService.removeDislikePost(userId, posterId, postId);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<Comment> updateCommentReaction(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody Map<String, String> payload
    ) {
        String action = payload.get("action");

        if ("Like".equals(action)) {
            return postService.addLikeComment(userId, posterId, postId, commentId);
        } else if ("Dislike".equals(action)) {
            return postService.addDislikeComment(userId, posterId, postId, commentId);
        } else if ("Unlike".equals(action)) {
            return postService.removeLikeComment(userId, posterId, postId, commentId);
        } else if ("Undislike".equals(action)) {
            return postService.removeDislikeComment(userId, posterId, postId, commentId);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/post/{userId}/{posterId}/{postId}/checkReactionPost")
    public ResponseEntity<String> checkReactionPost(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId
    ) {
        return postService.getReactionPost(userId, posterId, postId);
    }


    @GetMapping("/post/{userId}/{posterId}/{postId}/{commentId}/checkReactionComment")
    public ResponseEntity<String> checkReactionComment(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        return postService.getReactionComment(userId, posterId, postId, commentId);
    }



    @GetMapping("/post/{userId}/posts")
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

