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

    @GetMapping("/{userId}/allFriends")
    public ResponseEntity<List<AppUser>> getAllFriends(@PathVariable Long userId){
        return userserviceimpl.getFriends(userId);
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

    @GetMapping("/{userId}/allMessages")
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

    @GetMapping("/{userId}/postsByUsername/{username}")
    public ResponseEntity<List<Post>> getAllPosts(@PathVariable Long userId, @PathVariable String username) {
        return userserviceimpl.getPostsByUsername(userId, username);
    }



    @GetMapping("/{userId}/messagesByUsername/{username}")
    public ResponseEntity<List<Message>> getAllMessages(@PathVariable Long userId, @PathVariable String username) {
        return userserviceimpl.getMessagesByUsername(userId, username);
    }

    @GetMapping("/{userId}/getFriendRequests/")
    public ResponseEntity<List<AppUser>> getAllFriendRequests(@PathVariable Long userId) {
        return userserviceimpl.getAllFriendRequests(userId);
    }


    @PostMapping("/{userId}/createPost")
    public ResponseEntity<Post> createPost(
            @PathVariable Long userId,
            @RequestBody Post post
    ) {
        return userserviceimpl.createPost(userId, post);
    }

    @PutMapping("/{userId}/{friendId}")
    public ResponseEntity<AppUser> sendFriendRequestById(@PathVariable Long userId, @PathVariable Long friendRequestRecipient){
        return userserviceimpl.sendFriendRequest(userId, friendRequestRecipient);
    }

    @PostMapping("/{userId}/sendFriendRequestByUsername/{friendUsername}")
    public ResponseEntity<AppUser> sendFriendRequestByUsername(@PathVariable Long senderId, @PathVariable String friendUsername) {
        return userserviceimpl.sendFriendRequest(senderId, friendUsername);
    }

    @PutMapping("/{userId}/acceptFriendRequest/{potentialFriendUsername}")
    public ResponseEntity<AppUser> acceptFriendRequest(@PathVariable Long recipientId, @PathVariable String potentialFriendUsername) {
        return userserviceimpl.acceptFriendRequest(recipientId, potentialFriendUsername);
    }

    @PutMapping("/{userId}/declineFriendRequest/{potentialFriendUsername}")
    public ResponseEntity<AppUser> declineFriendRequest(@PathVariable Long recipientId, @PathVariable String potentialFriendUsername) {
        return userserviceimpl.declineFriendRequest(recipientId, potentialFriendUsername);
    }


    @GetMapping("/{userId}/posts/{targetUserId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId, @PathVariable Long targetUserId) {
        return userserviceimpl.getPostsByUserId(userId, targetUserId);
    }

    @GetMapping("/{userId}/postsByUsername/{friendUsername}")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return userserviceimpl.getPostsByUsername(userId, friendUsername);
    }
}
