package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.*;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import SoloProject.SocialMediaApp.models.PublicFeed;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class PostService {

    private final PublicFeed publicFeed;

    private final AppUserRepository repository;


    @Autowired
    public PostService(PublicFeed feed, AppUserRepository repository) {
        this.publicFeed = feed;
        this.repository = repository;
    }

    public ResponseEntity<List<Post>> getAllPublicPosts(){
        List<AppUser> users = repository.findAll();
        List<Post> websiteposts = new ArrayList<>();
        for(AppUser user : users){
            List<Post> userposts = user.getPosts();
                for(Post post : userposts){
                    if(!post.isFriendsonly()){
                        websiteposts.add(post);
                    }
                }
        }
        return ResponseEntity.ok(websiteposts);
    }

    public ResponseEntity<Post> getPostById(Long userId, Long postId, Long id) {
        AppUser appUser = repository.findByAppUserID(userId);
        if (appUser != null) {
            for (Post post : appUser.getPosts()) {
                if (post.getId().equals(postId)) {
                    return ResponseEntity.ok(post);
                }
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<List<Post>> getPostsByUserId(Long userId, Long friendId) {
        AppUser appUser = repository.findByAppUserID(userId);
        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }
        AppUser targetUser = repository.findByAppUserID(friendId);
        if (targetUser == null) {
            return ResponseEntity.notFound().build();
        }

        List<Long> friends = appUser.getFriends();
        boolean isFriend = false;
        for (Long friend : friends) {
            if (friend == (friendId)) {
                isFriend = true;
                break;
            }
        }

        if (!isFriend) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(targetUser.getPosts());
    }

    public ResponseEntity<List<Message>> getMessagesByUsername(Long userId, String targetUsername) {
        AppUser appUser = repository.findByAppUserID(userId);

        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }

        AppUser targetUser = repository.findByUsername(targetUsername);

        if (targetUser == null) {
            return ResponseEntity.notFound().build();
        }


        List<Long> friends = appUser.getFriends();
        boolean isFriend = false;
        for (Long friend : friends) {
            if (friend == targetUser.getAppUserID()) {
                isFriend = true;
                break;
            }
        }
        boolean isSelf = false;


        if (targetUser.getAppUserID() == appUser.getAppUserID()) {
            isSelf = true;
        }

        if (!isFriend && !isSelf) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Message> messages = new ArrayList<>();
        List<Message> incoming = appUser.getMessages();


        for (Message message : incoming) {
            if (message.getSender().equals(targetUsername)) {
                messages.add(message);
            }
        }

        return ResponseEntity.ok(messages);
    }

    public ResponseEntity<List<Post>> getPostsByUsername(Long userId, String targetUsername) {
        AppUser appUser = repository.findByAppUserID(userId);

        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }

        AppUser targetUser = repository.findByUsername(targetUsername);

        if (targetUser == null) {
            return ResponseEntity.notFound().build();
        }

        boolean isSelf = targetUser.getAppUserID().equals(appUser.getAppUserID());

        if (!isSelf) {
            List<Long> friends = appUser.getFriends();
            boolean isFriend = false;
            for (Long friend : friends) {
                if (friend == targetUser.getAppUserID()) {
                    isFriend = true;
                    break;
                }
            }

            if (!isFriend) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(targetUser.getPosts());
    }

    public ResponseEntity<Post> createPost(Long userId, Post post) {
        AppUser appUser = repository.findByAppUserID(userId);

        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }
        String username = appUser.getUsername();
        post.setAppUser(appUser);
        post.setPosterusername(username);
        post.setPosterid(appUser.getAppUserID());
        List<Post> userPosts = appUser.getPosts();
        userPosts.add(post);
        appUser.setPosts(userPosts);
        if(!post.isFriendsonly()){
        publicFeed.getFeed().add(post);
        }
        repository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }




    public ResponseEntity<List<Post>> getAllPosts(Long userId) {
        AppUser appUser = repository.findByAppUserID(userId);
        if (appUser != null) {
            return (ResponseEntity<List<Post>>) appUser.getPosts();
        } else {

            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<?> createComment(Long userId, Long postId, Comment comment) {
        AppUser appUser = repository.findByAppUserID(userId);

        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<Post> postOptional = appUser.getPosts().stream()
                .filter(post -> post.getId().equals(postId))
                .findFirst();

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            comment.setCommenterusername(appUser.getUsername());
            post.addComment(comment);
            repository.save(appUser);
            //have to attach the new post to the user.
            return ResponseEntity.status(HttpStatus.CREATED).body(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<Comment> handleCommentReaction(Comment comment, Long userId, String action) {
        HashMap<Long, String> reactions = comment.getReactions();
        String existingReaction = reactions.getOrDefault(userId, "None");

        if (action.equals(existingReaction)) {
            reactions.remove(userId);
        } else {
            reactions.put(userId, action);
        }

        return ResponseEntity.ok(comment);
    }

    public ResponseEntity<Post> handlePostReaction(Post post, Long userId, String action) {
        HashMap<Long, String> reactions = post.getReactions();
        String existingReaction = reactions.getOrDefault(userId, "None");

        if (action.equals(existingReaction)) {
            reactions.remove(userId);
        } else {
            reactions.put(userId, action);
        }

        return ResponseEntity.ok(post);
    }


    public ResponseEntity<List<Post>> getAllFriendPosts(Long userId) {
        AppUser appUser = repository.findByAppUserID(userId);
        List<Post> list = new ArrayList<>();
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //your posts.
        list.addAll(appUser.getPosts());
        for (Long friendid : appUser.getFriends()) {
            AppUser friend = repository.findByAppUserID(friendid);
            list.addAll(friend.getPosts());
        }
        return ResponseEntity.ok(list);
    }

@Transactional
    public ResponseEntity<?> deletePost(Long userId, Long postId) {
        // Check for valid user
        AppUser user = repository.findByAppUserID(userId);
        if(user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }

        // Find the post to be removed
        Post removePost = user.getPosts().stream()
                .filter(post -> post.getId().equals(postId))
                .findFirst()
                .orElse(null);

        if(removePost == null) {
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        }

        // Remove post from user's collection
        user.getPosts().remove(removePost);
    if (removePost.getAppUser() != null) {
        removePost.setAppUser(null);
    }
        // Remove from public feed if needed
        if(!removePost.friendsonly) {
            publicFeed.removeFromFeed(removePost);
        }

        // Save updated user
        try {
            repository.save(user);
            return new ResponseEntity<>(removePost, HttpStatus.OK);
        } catch(Exception ex) {
            // You can log the exception here for more detailed error diagnostics
            return new ResponseEntity<>("An error occurred while deleting the post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    public ResponseEntity<Comment> deleteComment(Long userId, Long postId, Long commentId) {
        AppUser user = repository.findByAppUserID(userId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Post targetPost = null;
        for (Post post : user.getPosts()) {
            if (post.getId().equals(postId)) {
                targetPost = post;
                break;
            }
        }

        if (targetPost == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Comment commentToRemove = null;
        for (Comment comment : targetPost.getCommentList()) {
            if (comment.getId().equals(commentId)) {
                commentToRemove = comment;
                break;
            }
        }

        if (commentToRemove == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        targetPost.getCommentList().remove(commentToRemove);
        repository.save(user);
        return ResponseEntity.ok(commentToRemove);
    }

}