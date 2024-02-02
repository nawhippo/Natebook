package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.*;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.CompressedImageRepository;
import SoloProject.SocialMediaApp.repository.NotificationRepository;
import SoloProject.SocialMediaApp.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PostService {

    private final AppUserRepository appUserRepository;
    private final PostRepository postRepository;
    private final CompressedImageRepository compressedImageRepository;
    private final NotificationRepository notificationRepository;
    private final CompressionService compressionService;

    @Autowired
    public PostService(AppUserRepository repository, NotificationRepository notificationRepository, PostRepository postRepository, CompressedImageRepository compressedImageRepository, CompressionService compressionService) {
        this.appUserRepository = repository;
        this.postRepository = postRepository;
        this.compressedImageRepository = compressedImageRepository;
        this.compressionService = compressionService;
        this.notificationRepository = notificationRepository;
    }


    //returns feed.
    public List<Post> getAllPublicPosts() {
        return postRepository.findByFriendsOnlyFalse();
    }



    public List<Post> getAllPublicPosts(Long userid) {
        AppUser user = appUserRepository.findByAppUserID(userid);
        Set<Long> following = new HashSet<>(user.getFollowing());
        Set<Long> friends = new HashSet<>(user.getFriends());

        List<Post> allPosts = postRepository.findAll();
        Collections.sort(allPosts, (post1, post2) -> {
            boolean isFriend1 = friends.contains(post1.getPosterId());
            boolean isFriend2 = friends.contains(post2.getPosterId());
            boolean isFollowing1 = following.contains(post1.getPosterId());
            boolean isFollowing2 = following.contains(post2.getPosterId());

            if ((isFriend1 && isFriend2) || (isFollowing1 && isFollowing2)) {
                return post1.getDateTime().compareTo(post2.getDateTime());
            }
            if (isFriend1 || isFollowing1) return -1;
            if (isFriend2 || isFollowing2) return 1;
            return post1.getDateTime().compareTo(post2.getDateTime());
        });

        return allPosts;
    }

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
    public Post createPost(Long userId, CreatePostRequest createPostRequest) {
        System.out.println("CREATING POST");
        Post post = createPostRequest.getPost();
        AppUser appUser = appUserRepository.findByAppUserID(userId);
        post.setPosterUsername(appUser.getUsername());
        post.setPosterId(appUser.getAppUserID());

        String description = post.getDescription();
        ArrayList<String> usernames = parseUsernames(description);
        ArrayList<Long> userids = new ArrayList<>();

        for (String username : usernames) {
            AppUser taggedUser = appUserRepository.findByUsername(username);
            if (taggedUser != null) {
                userids.add(taggedUser.getAppUserID());
                Notification notification = new Notification(taggedUser.getAppUserID(), "Post", post.getId());
                notificationRepository.save(notification);
            }
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy 'at' hh:mm:ss a");
        String formattedDateTime = now.format(formatter);
        post.setDateTime(formattedDateTime);
        postRepository.save(post);

        for(Long userid : appUser.getFollowers()){
            Notification notification = new Notification(userId, "Post", post.getId());
            notificationRepository.save(notification);
        }
        return post;
    }


    private ArrayList<String> parseUsernames(String text) {
        ArrayList<String> usernames = new ArrayList<>();
        Pattern pattern = Pattern.compile("@(\\w+)");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            usernames.add(matcher.group(1));
        }

        return usernames;
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
        for (CompressedImage compressedImage : compressedImageRepository.findByPostid(postId)) {
            compressedImageRepository.delete(compressedImage);
        }
        postRepository.deleteById(postId);
    }


    public ResponseEntity<Post> addImagesToPost(Long postId, List<String> base64Images) throws IOException, SQLException {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent() && base64Images != null && !base64Images.isEmpty()) {
            Post post = optionalPost.get();
            for (String base64Image : base64Images) {
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                String filename = "image_" + System.currentTimeMillis() + ".jpg";
                CompressedImage compressedImage = compressionService.compressImage(imageBytes, filename);
                post.addImage(compressedImage.getId());
                compressedImage.setPostid(postId);
                compressedImageRepository.save(compressedImage);
            }
            postRepository.save(post);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<?> getPostNotificationCount(Long userId){
        return ResponseEntity.ok(notificationRepository.countByUserIdAndNotificationType(userId, "Post"));
    }

    public ResponseEntity<?> getPostNotification(Long userId, Long postId){
        return ResponseEntity.ok(notificationRepository.findByUserIdAndObjectIdAndNotificationType(userId, postId, "Post"));
    }


    public List<Post> getAllUserPublicPosts(Long posterid) {
        AppUser appUser = appUserRepository.findByAppUserID(posterid);
        if (appUser.getFriends().contains(posterid)) {
            return postRepository.findByPosterId(posterid);
        } else {
            return postRepository.findByPosterIdAndFriendsOnlyFalse(posterid);
        }
    }
}