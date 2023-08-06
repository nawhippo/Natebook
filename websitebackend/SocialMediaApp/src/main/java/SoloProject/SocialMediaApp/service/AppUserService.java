package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.Post;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AppUserService {

    AppUser createUser(String firstName, String lastName, String username, String email, String password);


    ResponseEntity<List<AppUser>> getFriends(Long userId);
    ResponseEntity<AppUser> getFriend(Long userId, Long friendId);

    ResponseEntity<AppUser> getFriend(Long userId, String username);




    ResponseEntity<AppUser> findUser(Long id);

    ResponseEntity<AppUser> findUser(String username);

    ResponseEntity<AppUser> findByAppUserID(Long id);

    ResponseEntity<AppUser> saveUser (AppUser appUser);

    ResponseEntity<List<AppUser>> findUser(String firstname, String lastname);

    ResponseEntity<List<AppUser>> findRelatedUsers(String firstname, String lastname);

    ResponseEntity<Post> getPostById(Long userId, Long postId);
    ResponseEntity<List<Post>> getAllPosts(Long userId);

    ResponseEntity<Message> getMessageById(Long userId, Long messageId);
    ResponseEntity<List<Message>> getAllMessages(Long userId);

    ResponseEntity<List<AppUser>> getAllFriendRequests(Long UserId);

    ResponseEntity<AppUser> sendFriendRequest(Long senderId, Long friendId);

    ResponseEntity<AppUser> sendFriendRequest(Long senderId, String username);

    ResponseEntity<AppUser> acceptFriendRequest(Long recipientId, String potentialFriendUsername);

    ResponseEntity<AppUser> declineFriendRequest(Long recipientId, String potentialFriendUsername);

    ResponseEntity<Message> sendMessage(Long senderId, String content, List<Long> recipientIds);

    ResponseEntity<Post> createPost(Long userId, Post post);

    ResponseEntity<List<Post>> getPostsByUserId(Long userId, Long targetUserId);

    ResponseEntity<List<Post>> getPostsByUsername(Long userId, String targetUsername);

    ResponseEntity<List<Message>> getMessagesByUsername(Long userId, String targetUsername);
}