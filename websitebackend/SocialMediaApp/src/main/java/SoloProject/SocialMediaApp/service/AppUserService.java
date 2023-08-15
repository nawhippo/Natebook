package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.models.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AppUserService {

    AppUser createUser(String firstName, String lastName, String username, String email, String password);


    ResponseEntity<List<Long>> getFriends(Long userId);
    ResponseEntity<AppUser> getFriend(Long userId, Long friendId);

    ResponseEntity<AppUser> getFriend(Long userId, String username);




    ResponseEntity<AppUser> findUser(Long id);

    ResponseEntity<AppUser> findUser(String username);

    ResponseEntity<AppUser> findByAppUserID(Long id);

    ResponseEntity<AppUser> saveUser (AppUser appUser);

    ResponseEntity<List<AppUser>> findRelatedUsers(String firstname, String lastname);

    ResponseEntity<Post> getPostById(Long userId, Long postId);
    ResponseEntity<List<Post>> getAllPosts(Long userId);

    ResponseEntity<Message> getMessageById(Long userId, Long messageId);
    ResponseEntity<List<Message>> getAllMessages(Long userId);

    ResponseEntity<List<Long>> getAllFriendRequests(Long UserId);

    ResponseEntity<List<UserDTO>> getAllFriendsDTOS(Long UserId);

    ResponseEntity<List<UserDTO>> getAllFriendRequestsDTOS(Long UserId);

    ResponseEntity<AppUser> sendFriendRequest(Long senderId, Long friendId);

    ResponseEntity<AppUser> sendFriendRequest(Long senderId, String username);

    ResponseEntity<Message> sendMessage(Long senderId, String content, List<Long> recipientIds);

    ResponseEntity<AppUser> acceptFriendRequest(Long recipientId, Long senderId);

    ResponseEntity<AppUser> declineFriendRequest(Long recipientId, Long senderId);

    ResponseEntity<Post> createPost(Long userId, Post post);

    ResponseEntity<List<Post>> getPostsByUserId(Long userId, Long targetUserId);

    ResponseEntity<List<Post>> getPostsByUsername(Long userId, String targetUsername);

    ResponseEntity<List<Message>> getMessagesByUsername(Long userId, String targetUsername);

    ResponseEntity<List<AppUser>> getAllUsers();

    ResponseEntity<AppUser> getAccountDetails(Long userid);


    ResponseEntity<AppUser> updateAccountDetails(Long userId, String newFirstName, String newLastName, String newEmail);
}