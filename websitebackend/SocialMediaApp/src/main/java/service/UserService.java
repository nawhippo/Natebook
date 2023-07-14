package service;

import models.Message;
import models.Post;
import models.User;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.ReflectPermission;
import java.util.List;

public interface UserService {

    User createUser(String firstname, String lastname, String username, String email);

    ResponseEntity<User> findByUserID(Long id);

    ResponseEntity<List<User>> getFriends(Long userId);
    ResponseEntity<List<User>> findByFirstname(String firstname);
    ResponseEntity<List<User>> findByLastname(String firstname);
    ResponseEntity<List<User>> findByFirstNameAndLastName(String firstname, String lastname);

    ResponseEntity<User> saveUser (User user);

    ResponseEntity<Post> getPostById(Long userId, Long postId);
    ResponseEntity<List<Post>> getAllPosts(Long userId);

    ResponseEntity<Message> getMessageById(Long MessageId, Long userId);
    ResponseEntity<List<Message>> getAllMessages(Long userid);




}
