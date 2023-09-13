package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
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

    @DeleteMapping("/post/{username}/{postId}/deletePost")
    public ResponseEntity<?> deletePost(@PathVariable String username, @PathVariable Long postId){
        return postService.deletePost(username, postId);
    }




    @GetMapping("/post/{userId}/{posterId}/{postId}")
    public ResponseEntity<Post> getPostById(
            @PathVariable Long userId,
            @PathVariable Long posterId,
            @PathVariable Long postId) {
        return postService.getPostById(userId, posterId, postId);
    }

    @PutMapping("/post/{reactorId}/{posterId}/{postId}/updateReactionPost")
    public ResponseEntity<Post> updateReactionPost(
            @PathVariable Long reactorId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @RequestBody Map<String, String> payload) {

        String action = payload.get("action");

        ResponseEntity<Post> responseEntity = postService.handlePostReaction(reactorId, posterId, postId, action);
        Post updatedPost = responseEntity.getBody();

        if (updatedPost != null) {
            return new ResponseEntity<>((Post) updatedPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/post/{reactorId}/{posterId}/{postId}/{commentId}/updateReactionComment")
            public ResponseEntity<Comment> updateCommentReaction(
            @PathVariable Long reactorId,
            @PathVariable Long posterId,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody Map<String, String> payload) {

        String action = payload.get("action");

        ResponseEntity<Comment> responseEntity = postService.handleCommentReaction(reactorId, posterId, postId, commentId, action);
        Comment updatedComment = responseEntity.getBody();

        if (updatedComment != null) {
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
















    @PostMapping("/post/{posterUsername}/{postId}/createComment")
    public ResponseEntity<?> createComment(
            @PathVariable String posterUsername,
            @PathVariable Long postId,
            @RequestBody Comment comment
    ) {
        comment.setCommenterusername(comment.getCommenterusername());
        return postService.createComment(posterUsername, postId, comment);
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


    @DeleteMapping("/post/{username}/{postId}/{commentId}/deleteComment")
    public ResponseEntity<Comment> deleteComment(@PathVariable String username, @PathVariable Long postId, @PathVariable Long commentId){
        return postService.deleteComment(username, postId, commentId);
    }

    }

