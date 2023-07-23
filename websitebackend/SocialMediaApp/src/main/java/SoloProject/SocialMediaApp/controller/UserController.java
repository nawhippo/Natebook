package SoloProject.SocialMediaApp.controller;
import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import SoloProject.SocialMediaApp.service.AppUserServiceImpl;

import java.util.List;

@RestController
public class UserController {

    @GetMapping
    public String helloWorld(){
        return "Hello, user!";
    }
    private final AppUserServiceImpl userserviceimpl;
    @Autowired
    public UserController(AppUserServiceImpl userserviceimpl) {
        this.userserviceimpl = userserviceimpl;
    }
    private AppUserRepository appUserRepository;

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<AppUser>> getAllFriends(@PathVariable Long userId){
        return userserviceimpl.getFriends(userId);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<AppUser> findUserbyId(@PathVariable Long userId){

        return userserviceimpl.findByAppUserID(userId);
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
    public void createAccount(@RequestBody AppUser appUser){

        userserviceimpl.saveUser(appUser);
    }

    @PostMapping("/{userId}/sendMessage")
    public ResponseEntity<Message> sendMessage(
            @PathVariable Long userId,
            @RequestParam String content,
            @RequestParam List<Long> recipientIds
    ) {
        return userserviceimpl.sendMessage(userId, content, recipientIds);
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<Post>> getAllPosts(@PathVariable Long userId) {
        return userserviceimpl.getAllPosts(userId);
    }
}
