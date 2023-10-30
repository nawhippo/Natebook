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
    public List<Post> getAllPublicPosts(){
    return postRepository.findByFriendsOnlyFalse();
    }

    //all of the posts from a user's friends.
    public List<Post> getAllFriendPosts(Long userId) {
        AppUser appUser = appUserRepository.findByAppUserID(userId);
        List<Post> megalist = new ArrayList<>();
        for(Long friendid : appUser.getFriends()) {
            List<Post> friendpostlist = postRepository.findByPosterId(friendid);
            megalist.addAll(friendpostlist);
        }
        return megalist;
    }


    public List<Post> getPostsByPosterId(Long userid, Long posterid) {
        AppUser appUser = appUserRepository.findByAppUserID(userid);
        if(appUser.getFriends().contains(posterid)){
            return postRepository.findByPosterId(posterid);
        } else {
            return postRepository.findByPosterIdAndFriendsOnlyFalse(posterid);
        }
    }

    public List<Post> getPostsByPosterUsername(Long userid, String posterUsername) {
        AppUser appUser = appUserRepository.findByAppUserID(userid);
        AppUser friend = appUserRepository.findByUsername(posterUsername);
        Long friendid = friend.getAppUserID();
        if(appUser.getFriends().contains(friendid)){
            return postRepository.findByPosterUsername(posterUsername);
        } else {
            return postRepository.findByPosterUsernameAndFriendsOnlyFalse(posterUsername);
        }
    }



    public Post createPost(Long userId, Post post) {
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
    }

    public Optional<Post> getPostById(Long postid) {

        return postRepository.findById(postid);
    }



public ResponseEntity<Post> addImagestoPost(Long postId, ArrayList<MultipartFile> Photos) throws IOException {
        Optional<Post> optionalPost = postRepository.findById(postId);
        for(MultipartFile photo : Photos){
          CompressedImage compressedImage = compressionService.compressImage(photo);
          compressedImage.setAttachedToId(postId);
          compressedImageRepository.save(compressedImage);
        }
        return (ResponseEntity<Post>) ResponseEntity.notFound();
}
}