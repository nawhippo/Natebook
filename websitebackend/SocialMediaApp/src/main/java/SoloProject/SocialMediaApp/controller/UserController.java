package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api") // The base path for all endpoints in this controller
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

    @PutMapping("/{userId}/{friendId}")
    public ResponseEntity<AppUser> sendFriendRequestById(@PathVariable Long userId, @PathVariable Long friendRequestRecipient){
        return userserviceimpl.sendFriendRequest(userId, friendRequestRecipient);
    }

    @PutMapping("/{userId}/addFriend/{friendUsername}")
    public ResponseEntity<AppUser> sendFriendRequestByUsername(@PathVariable Long userId, @PathVariable String friendRequestRecipient){
        return userserviceimpl.sendFriendRequest(userId, friendRequestRecipient);
    }


    @GetMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<AppUser> getFriendById(@PathVariable Long userId, @PathVariable Long friendId) {
        return userserviceimpl.getFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/{friendUsername}")
    public ResponseEntity<AppUser> getFriendByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return userserviceimpl.getFriend(userId, friendUsername);
    }










    //TODO: try to figure this out
    @GetMapping("/user")
    public ResponseEntity<AppUser> getUserData() {
        // Simulate login and fetch user data based on user ID (e.g., 1)
        Long userId = 1L;
        AppUser user = appUserRepository.findById(userId).orElse(null);

        if (user != null) {
            // If the user is found, return the user data with a 200 OK response
            return ResponseEntity.ok(user);
        } else {
            // If the user is not found, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }
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
    public void createAccount(@RequestBody Map<String, String> formData) {

        String firstName = formData.get("firstname");
        String lastName = formData.get("lastname");
        String email = formData.get("email");
        String password = formData.get("password");
        String username = formData.get("username");

        AppUser appUser = new AppUser(firstName, lastName, email, password, username);
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
