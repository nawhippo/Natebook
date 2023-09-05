package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.*;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import SoloProject.SocialMediaApp.models.PublicFeed;
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



    public ResponseEntity<?> handleReaction(Long userId, Long posterId, Long postId, Long commentId, String action) {
        AppUser poster = repository.findByAppUserID(posterId);

        if (poster == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<Post> postOpt = poster.getPosts().stream().filter(p -> p.getId().equals(postId)).findFirst();

        if (!postOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Post post = postOpt.get();

        if (commentId != null) {
            Optional<Comment> commentOpt = post.getCommentList().stream().filter(c -> c.getId().equals(commentId)).findFirst();
            if (!commentOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Comment comment = commentOpt.get();
            return handleCommentReaction(comment, userId, action);
        } else {
            return handlePostReaction(post, userId, action);
        }
    }


    private ResponseEntity<Comment> handleCommentReaction(Comment comment, Long userId, String action) {
        HashMap<Long, String> reactions = comment.getReactions();
        String existingReaction = reactions.getOrDefault(userId, "None");

        if (action.equals(existingReaction)) {
            reactions.remove(userId);
        } else {
            reactions.put(userId, action);
        }

        return ResponseEntity.ok(comment);
    }

    private ResponseEntity<Post> handlePostReaction(Post post, Long userId, String action) {
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

    public ResponseEntity<Post> deletePost(Long userId, Long postId) {
        AppUser user = repository.findByAppUserID(userId);
        for (Post post : user.getPosts()) {
            if (post.getId() == postId) {
                user.getPosts().remove(post);
                if(!post.friendsonly) {
                    publicFeed.removeFromFeed(post);
                }
                repository.save(user);
                return ResponseEntity.ok(post);
            }
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Post> deleteComment(Long userId, Long postId, Long commentId) {
        AppUser user = repository.findByAppUserID(userId);
        for (Post post : user.getPosts()) {
            if (post.getId() == postId) {
                for (Comment comment : post.getCommentList()) {
                    if (comment.getId() == commentId) {
                        post.removeComment(comment);
                        repository.save(user);
                        return ResponseEntity.ok(post);
                    }
                }
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}