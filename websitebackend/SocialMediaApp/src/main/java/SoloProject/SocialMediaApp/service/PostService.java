package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.CompressedImage;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.CompressedImageRepository;
import SoloProject.SocialMediaApp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Service
public class PostService {

    private final AppUserRepository appUserRepository;
    private final PostRepository postRepository;
    private final CompressedImageRepository compressedImageRepository;

    private final CompressionService compressionService;

    @Autowired
    public PostService(AppUserRepository repository, PostRepository postRepository, CompressedImageRepository compressedImageRepository, CompressionService compressionService) {
        this.appUserRepository = repository;
        this.postRepository = postRepository;
        this.compressedImageRepository = compressedImageRepository;
        this.compressionService = compressionService;
    }


    //returns feed.
    public List<Post> getAllPublicPosts() {
        return postRepository.findByFriendsOnlyFalse();
    }

    //all of the posts from a user's friends.
    public List<Post> getAllFriendPosts(Long userId) {
        AppUser appUser = appUserRepository.findByAppUserID(userId);
        List<Post> megalist = new ArrayList<>();
        for (Long friendid : appUser.getFriends()) {
            List<Post> friendpostlist = postRepository.findByPosterId(friendid);
            megalist.addAll(friendpostlist);
        }
        return megalist;
    }


    public List<Post> getPostsByPosterId(Long userid, Long posterid) {
        AppUser appUser = appUserRepository.findByAppUserID(userid);
        if (appUser.getFriends().contains(posterid)) {
            return postRepository.findByPosterId(posterid);
        } else {
            return postRepository.findByPosterIdAndFriendsOnlyFalse(posterid);
        }
    }

    public List<Post> getPostsByPosterUsername(Long userid, String posterUsername) {
        AppUser appUser = appUserRepository.findByAppUserID(userid);
        AppUser friend = appUserRepository.findByUsername(posterUsername);
        Long friendid = friend.getAppUserID();
        if (appUser.getFriends().contains(friendid)) {
            return postRepository.findByPosterUsername(posterUsername);
        } else {
            return postRepository.findByPosterUsernameAndFriendsOnlyFalse(posterUsername);
        }
    }


    @Transactional
    public Post createPost(Long userId, Post post) {
        System.out.println("CREATING POST");
        AppUser appUser = appUserRepository.findByAppUserID(userId);
        post.setPosterUsername(appUser.getUsername());
        post.setPosterAppUserId(appUser.getAppUserID());
        return postRepository.save(post);
    }


    public Post handlePostReaction(Long postId, Long reactorId, String action) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (!optionalPost.isPresent()) {
            throw new NoSuchElementException("Post not found");
        }

        Post post = optionalPost.get();
        Map<Long, String> reactions = post.getReactions();
        String existingReaction = reactions.getOrDefault(reactorId, "None");

        if (existingReaction.equals(action)) {
            post.removeReaction(reactorId);
        } else {
            if (!existingReaction.equals("None")) {
                post.removeReaction(reactorId);
            }
            post.addReaction(reactorId, action);
        }

        return postRepository.save(post);
    }


    //it'll display this on the front end conditionally
    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
        for(CompressedImage compressedImage: compressedImageRepository.findByPostid(postId)){
            compressedImageRepository.delete(compressedImage);
        }
            postRepository.deleteById(postId);
    }


    public ResponseEntity<Post> addImagestoPost(Long postId, List<String> base64Images) throws IOException, SQLException {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent() && base64Images != null && !base64Images.isEmpty()) {
            Post post = optionalPost.get();
            for (String base64Image : base64Images) {
                // Decode the base64 string to a byte array
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                String filename = "image_" + System.currentTimeMillis() + ".jpg"; // Generate a filename
                CompressedImage compressedImage = compressionService.compressImage(imageBytes, filename);
                compressedImage.setPostid(postId); // Assuming you want to link the image to the post
                compressedImageRepository.save(compressedImage);
            }

            postRepository.save(post); // Save the post if needed
            return ResponseEntity.ok(post);
        }

        return ResponseEntity.notFound().build();
    }
}