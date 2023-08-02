package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.Post;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AppUserService {

    AppUser createUser(String firstName, String lastName, String username, String email, String password);

    ResponseEntity<AppUser> findByAppUserID(Long id);

    ResponseEntity<List<AppUser>> getFriends(Long userId);
    ResponseEntity<AppUser> getFriendById(Long userId, Long friendId);

    ResponseEntity<AppUser> findByUsername(String username);

    ResponseEntity<AppUser> addFriend(Long userId, String username);

    ResponseEntity<List<AppUser>> findByFirstname(String firstname);
    ResponseEntity<List<AppUser>> findByLastname(String firstname);
    ResponseEntity<List<AppUser>> findByFirstNameAndLastName(String firstname, String lastname);

    ResponseEntity<AppUser> saveUser (AppUser appUser);

    ResponseEntity<Post> getPostById(Long userId, Long postId);
    ResponseEntity<List<Post>> getAllPosts(Long userId);

    ResponseEntity<Message> getMessageById(Long MessageId, Long userId);
    ResponseEntity<List<Message>> getAllMessages(Long userid);

    ResponseEntity<Message> sendMessage(Long senderId, String content, List<Long> recipientIds);


}
