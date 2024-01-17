package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.*;
import SoloProject.SocialMediaApp.repository.*;
import SoloProject.SocialMediaApp.service.AccountService;
import SoloProject.SocialMediaApp.service.CompressionService;
import SoloProject.SocialMediaApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    private final CompressionService compressionService;
    private final CommentRepository commentRepository;
    private final CompressedImageRepository compressedImageRepository;
    private final PostRepository postRepository;

    private final NotificationRepository notificationRepository;

    private final AccountService accountService;

    private final AppUserRepository appUserRepository;
    @Autowired
    public PostController(PostService postService, NotificationRepository notificationRepository, PostRepository postRepository, CompressionService compressionService, CompressedImageRepository compressedImageRepository, CommentRepository commentRepository, AccountService accountService, AppUserRepository appUserRepository) {
        this.postService = postService;
        this.compressedImageRepository = compressedImageRepository;
        this.commentRepository = commentRepository;
        this.compressionService = compressionService;
        this.postRepository = postRepository;
        this.accountService = accountService;
        this.appUserRepository = appUserRepository;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable("postId") Long postId) {
        List<Comment> comments = commentRepository.findByPostid(postId);
        if (comments != null && !comments.isEmpty()) {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/post/{postId}/images")
    @Transactional
    public ResponseEntity<List<CompressedImage>> getImages(@PathVariable("postId") Long postId) {
        List<CompressedImage> images = compressedImageRepository.findByPostid(postId);
        if (images != null && !images.isEmpty()) {
            return new ResponseEntity<>(images, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/post/{userId}/createPost")
    public ResponseEntity<?> createPost(
            @PathVariable Long userId,
            @RequestBody CreatePostRequest createPostRequest
    ) throws IOException, SQLException {
        Post createdPost = postService.createPost(userId, createPostRequest);

        if (createdPost == null) {
            return new ResponseEntity<>("Failed to create post", HttpStatus.BAD_REQUEST);
        }

        if (createPostRequest.getImages() != null && !createPostRequest.getImages().isEmpty()) {
            postService.addImagesToPost(createdPost.getId(), createPostRequest.getImages());
        }

        if (createdPost != null) {
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }




    @DeleteMapping("/post/{postId}/deletePost")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, Authentication authentication) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            if (!accountService.checkAuthenticationMatch(post.get().getPosterUsername(), authentication)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
            }
            postService.deletePost(postId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PutMapping("/post/{postId}/{reactorId}/updateReactionPost")
    public ResponseEntity<?> updateReactionPost(
            @PathVariable Long reactorId,
            @PathVariable Long postId,
            @RequestBody Map<String, String> payload,
            Authentication authentication) {

        AppUser appUser = appUserRepository.findByAppUserID(reactorId);
        if (!accountService.checkAuthenticationMatch(appUser.getUsername(), authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        String action = payload.get("action");
        AppUser reactor = appUserRepository.findById(reactorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reactor not found"));

        Post updatedPost = postService.handlePostReaction(postId, reactorId, action);
        if (updatedPost != null) {
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




    @GetMapping("/post/{userId}/posts/{targetUserId}")
    public ResponseEntity<List<Post>> getAllPostsByUserId(@PathVariable Long userId, @PathVariable Long targetUserId) {
        List<Post> posts = postService.getPostsByPosterId(userId, targetUserId);

        if (posts != null && !posts.isEmpty()) {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/posts/{targetUserId}")
    public ResponseEntity<List<Post>> getAllPublicPosts(@PathVariable Long targetUserId) {
        List<Post> posts = postService.getAllUserPublicPosts(targetUserId);

        if (posts == null) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/notifications/posts/{userId}/getPostsNotification")
    public ResponseEntity<?> getUserPostNotificationCount(@PathVariable Long userId, Authentication authentication) {
        System.out.println("Request userid: " + userId);
        String username = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(username, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return postService.getPostNotificationCount(userId);
    }

    @GetMapping("/notifications/posts/{userId}/{postId}/getIndividualPostNotification")
    public ResponseEntity<?> getUserPostNotification(@PathVariable Long userId, @PathVariable Long postId, Authentication authentication){
        System.out.println("Request userid: " + userId);
        String username = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(username, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return postService.getPostNotification(userId, postId);
    }

    @DeleteMapping("/notifications/posts/{userId}/delete/{postId}")
    public ResponseEntity<?> deletePostNotification(@PathVariable Long userId, @PathVariable Long postId, Authentication authentication){
        String username = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(username, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        notificationRepository.deleteByObjectIdAndUserIdAndType(postId, userId, "Tag");
        return ResponseEntity.ok().build();
    }


        @GetMapping("/post/{userId}/postsByUsername/{posterUsername}")
        public ResponseEntity<List<Post>> getAllPostsByUsername(@PathVariable Long userId, @PathVariable String posterUsername) {
            List<Post> posts = postService.getPostsByPosterUsername(userId, posterUsername);
            if (posts != null && !posts.isEmpty()) {
                return new ResponseEntity<>(posts, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

