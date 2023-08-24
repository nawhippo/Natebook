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

    @PostMapping("/post/{userId}/createPost")
    public ResponseEntity<Post> createPost(
            @PathVariable Long userId,
            @RequestBody Post post
    ) {
        return userserviceimpl.createPost(userId, post);
    }

    @GetMapping("/post/{userId}/{posterId}/{postId}")
    public ResponseEntity<Post> getPostById(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId) {
        return userserviceimpl.getPostById(userId, posterId, postId);
    }

    @PutMapping("/post/{userId}/{posterId}/{postId}/addLike")
    public ResponseEntity<Post> addLikePost(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId
            ) {
        return userserviceimpl.addLikePost(userId, posterId, postId);
    }


    @PutMapping("/post/{userId}/{posterId}/{postId}/addDislike")
    public ResponseEntity<Post> addDislikePost(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId
           ) {
        return userserviceimpl.addDislikePost(userId, posterId, postId);
    }



    @PutMapping("/post/{userId}/{posterId}/{postId}/removeLike")
    public ResponseEntity<Post> RemoveLikePost(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId
           ) {
        return userserviceimpl.removeLikePost(userId, posterId, postId);
    }

    @PutMapping("/post/{userId}/{posterId}/{postId}/removeDislike")
    public ResponseEntity<Post> RemoveDislikePost(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId) {
        return userserviceimpl.removeDislikePost(userId, posterId, postId);
    }


    @PostMapping("/post/{userId}/{postId}/createComment")
    public ResponseEntity<?> createComment(
            @PathVariable Long userId,
            @PathVariable Long postId,
            @RequestBody Comment comment
    ) {
        return userserviceimpl.createComment(userId, postId, comment);
    }

    @PutMapping("/post/{userId}/{posterId}/{postId}/addLikeComment")
    public ResponseEntity<Comment> addLikeComment(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        return userserviceimpl.addLikeComment(userId, posterId, postId, commentId);
    }


    @PutMapping("/post/{userId}/{posterId}/{postId}/addDislikeComment")
    public ResponseEntity<Comment> addDislikeComment(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        return userserviceimpl.addDislikeComment(userId, posterId, postId, commentId);
    }



    @PutMapping("/post/{userId}/{posterId}/{postId}/removeLikeComment")
    public ResponseEntity<Comment> RemoveLikeComment(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @PathVariable Long commentId

    ) {
        return userserviceimpl.removeLikeComment(userId, posterId, postId, commentId);
    }

    @PutMapping("/post/{userId}/{posterId}/{postId}/removeDislikeComment")
    public ResponseEntity<Comment> RemoveDislikeComment(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        return userserviceimpl.removeDislikeComment(userId, posterId, postId, commentId);
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
