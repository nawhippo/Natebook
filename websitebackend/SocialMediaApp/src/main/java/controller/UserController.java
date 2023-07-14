package controller;
import models.Message;
import models.Post;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import repository.*;
import service.UserService;
import service.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    private  UserRepository userRepository;
    private UserServiceImpl userserviceimpl;
    @GetMapping
    public String handleRequest(){
        return "Welcome to the users page!";
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<User>> getAllFriends(@PathVariable Long userId){
        return userserviceimpl.getFriends(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUserbyId(@PathVariable Long userId){

        return userserviceimpl.findByUserID(userId);
    }

    @GetMapping("/{userId}/messages")
    public ResponseEntity<List<Message>> getAllMessages(@PathVariable Long userId){
        return userserviceimpl.getAllMessages(userId);
    }

    @GetMapping("/{userId}/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long userId, @PathVariable Long messageId) {
        return userserviceimpl.getMessageById(userId, messageId);
    }


    @GetMapping("/{userId}/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long userId, @PathVariable Long postId) {
        return userserviceimpl.getPostById(userId, postId);
    }

    @PostMapping("/createAccount")
    public void createAccount(@RequestBody User user){

        userService.saveUser(user);
    }
}
