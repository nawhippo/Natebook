package service;

import models.Message;
import models.Post;
import models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import repository.UserRepository;

import java.util.List;


//intermediary between model and database (repository)
public class UserServiceImpl implements UserService {

    UserRepository repository;


    @Override
    public User createUser(String firstName, String lastName, String username, String email){
    User newuser = new User(firstName, lastName, username, email);
    saveUser(newuser);
    return newuser;
    }

    @Override
    public ResponseEntity<User> saveUser(User user) {
        repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Override
    public ResponseEntity<User> findByUserID(Long id) {
        User user = repository.findByUserID(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<List<User>> findByFirstname(String firstname) {
        List<User> users = repository.findByFirstname(firstname);
        return ResponseEntity.ok(users);

    }

    @Override
    public ResponseEntity<List<User>> findByLastname(String lastname) {
        List<User> users = repository.findByLastname(lastname);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<List<User>> findByFirstNameAndLastName(String firstname, String lastname) {
        List<User> users = repository.findByFirstnameAndLastname(firstname, lastname);
        return ResponseEntity.ok(users);
    }


    //Post functions
    @Override
    public ResponseEntity<Post> getPostById(Long userId, Long postId){
        User user = findByUserID(userId).getBody();
        if (user != null) {
            for (Post post : user.getPosts()) {
                if (post.getId().equals(postId)) {
                    return ResponseEntity.ok(post);
                }
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Message> getMessageById(Long userId, Long messageId){
        User user = findByUserID(userId).getBody();
        if (user != null) {
            for (Message message : user.getMessages()) {
                if (message.getId().equals(messageId)) {
                    return ResponseEntity.ok(message);
                }
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }




    @Override
    public ResponseEntity<List<Post>> getAllPosts(Long userId) {
        User user = findByUserID(userId).getBody();
        if (user != null) {
            return (ResponseEntity<List<Post>>) user.getPosts();
        } else {

            return ResponseEntity.notFound().build();
        }
    }



    @Override
    public ResponseEntity<List<Message>> getAllMessages(Long userId) {
        User user = findByUserID(userId).getBody();
        if (user != null) {
            return (ResponseEntity<List<Message>>) user.getMessages();
        } else {

            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<List<User>> getFriends(Long userId) {
        User user = findByUserID(userId).getBody();
        return (ResponseEntity<List<User>>) user.getFriends();
    }


}