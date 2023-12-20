package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.Comment;
import SoloProject.SocialMediaApp.models.CompressedImage;
import SoloProject.SocialMediaApp.models.CreatePostRequest;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.CommentRepository;
import SoloProject.SocialMediaApp.repository.CompressedImageRepository;
import SoloProject.SocialMediaApp.repository.PostRepository;
import SoloProject.SocialMediaApp.service.CompressionService;
import SoloProject.SocialMediaApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    public PostController(PostService postService, PostRepository postRepository, CompressionService compressionService, CompressedImageRepository compressedImageRepository, CommentRepository commentRepository) {
        this.postService = postService;
        this.compressedImageRepository = compressedImageRepository;
        this.commentRepository = commentRepository;
        this.compressionService = compressionService;
        this.postRepository = postRepository;
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
    public ResponseEntity<Post> createPost(
            @PathVariable Long userId,
            @RequestBody CreatePostRequest createPostRequest // Assuming CreatePostRequest contains the Post and List<String> of base64 images
    ) throws IOException, SQLException {
        Post createdPost = postService.createPost(userId, createPostRequest.getPost());

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
    public ResponseEntity<?> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/post/{postId}/{reactorId}/updateReactionPost")
    public ResponseEntity<Post> updateReactionPost(
            @PathVariable Long reactorId,
            @PathVariable Long postId,
            @RequestBody Map<String, String> payload) {

        String action = payload.get("action");

        Post updatedPost = postService.handlePostReaction(postId, reactorId, action);

        if (updatedPost != null) {
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/post/{userId}/friendPosts")
    public ResponseEntity<List<Post>> getAllFriendPosts(@PathVariable Long userId) {
        List<Post> posts = postService.getAllFriendPosts(userId);
        if (posts != null && !posts.isEmpty()) {
            return new ResponseEntity<>(posts, HttpStatus.OK);
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

