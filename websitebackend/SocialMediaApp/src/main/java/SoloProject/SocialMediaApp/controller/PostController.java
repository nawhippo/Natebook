package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.Comment;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AppUserService;
import SoloProject.SocialMediaApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/post/{userId}/{posterId}/{postId}/addLike")
    public ResponseEntity<Post> addLikePost(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId
            ) {
        return postService.addLikePost(userId, posterId, postId);
    }


    @PutMapping("/post/{userId}/{posterId}/{postId}/addDislike")
    public ResponseEntity<Post> addDislikePost(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId
           ) {
        return postService.addDislikePost(userId, posterId, postId);
    }



    @PutMapping("/post/{userId}/{posterId}/{postId}/removeLike")
    public ResponseEntity<Post> RemoveLikePost(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId
           ) {
        return postService.removeLikePost(userId, posterId, postId);
    }

    @PutMapping("/post/{userId}/{posterId}/{postId}/removeDislike")
    public ResponseEntity<Post> RemoveDislikePost(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId) {
        return postService.removeDislikePost(userId, posterId, postId);
    }


    @PostMapping("/post/{userId}/{postId}/createComment")
    public ResponseEntity<?> createComment(
            @PathVariable Long userId,
            @PathVariable Long postId,
            @RequestBody Comment comment
    ) {
        return postService.createComment(userId, postId, comment);
    }

    @PutMapping("/post/{userId}/{posterId}/{postId}/addLikeComment")
    public ResponseEntity<Comment> addLikeComment(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        return postService.addLikeComment(userId, posterId, postId, commentId);
    }


    @PutMapping("/post/{userId}/{posterId}/{postId}/addDislikeComment")
    public ResponseEntity<Comment> addDislikeComment(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        return postService.addDislikeComment(userId, posterId, postId, commentId);
    }



    @PutMapping("/post/{userId}/{posterId}/{postId}/removeLikeComment")
    public ResponseEntity<Comment> RemoveLikeComment(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @PathVariable Long commentId

    ) {
        return postService.removeLikeComment(userId, posterId, postId, commentId);
    }

    @PutMapping("/post/{userId}/{posterId}/{postId}/removeDislikeComment")
    public ResponseEntity<Comment> RemoveDislikeComment(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        return postService.removeDislikeComment(userId, posterId, postId, commentId);
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
}
