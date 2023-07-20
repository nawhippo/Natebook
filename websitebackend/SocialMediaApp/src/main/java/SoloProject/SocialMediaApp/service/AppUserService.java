package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.Post;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AppUserService {

    AppUser createUser(String firstname, String lastname, String username, String email);

    ResponseEntity<AppUser> findByAppUserID(Long id);

    ResponseEntity<List<AppUser>> getFriends(Long userId);
    ResponseEntity<List<AppUser>> findByFirstname(String firstname);
    ResponseEntity<List<AppUser>> findByLastname(String firstname);
    ResponseEntity<List<AppUser>> findByFirstNameAndLastName(String firstname, String lastname);

    ResponseEntity<AppUser> saveUser (AppUser appUser);

    ResponseEntity<Post> getPostById(Long userId, Long postId);
    ResponseEntity<List<Post>> getAllPosts(Long userId);

    ResponseEntity<Message> getMessageById(Long MessageId, Long userId);
    ResponseEntity<List<Message>> getAllMessages(Long userid);




}
