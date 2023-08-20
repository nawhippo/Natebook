package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.models.UserDTO;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    private AppUserServiceImpl userserviceimpl;
    @Autowired
    public UserController(AppUserServiceImpl userserviceimpl) {
        this.userserviceimpl = userserviceimpl;
    }
    private AppUserRepository appUserRepository;

    @GetMapping
    public String helloWorld() {
        return "Hello, user!";
    }

    @GetMapping("/friends/{userId}/getAllFriends")
    public ResponseEntity<List<UserDTO>> getAllFriends(@PathVariable Long userId) {
        return userserviceimpl.getAllFriendsDTOS(userId);
    }

    @GetMapping("/friends/{userId}/{friendId}")
    public ResponseEntity<AppUser> getFriendById(@PathVariable Long userId, @PathVariable Long friendId) {
        return userserviceimpl.getFriend(userId, friendId);
    }

    @GetMapping("/friends/{userId}/{friendUsername}")
    public ResponseEntity<AppUser> getFriendByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return userserviceimpl.getFriend(userId, friendUsername);
    }

    @GetMapping("/account/{userId}/accountDetails")
    public ResponseEntity<AppUser> getAccountDetails(@PathVariable Long userId) {
        return userserviceimpl.getAccountDetails(userId);
    }

    @PostMapping("account/createAccount")
    public void createAccount(@RequestBody Map<String, String> formData) {
        String firstName = formData.get("firstname");
        String lastName = formData.get("lastname");
        String email = formData.get("email");
        String password = formData.get("password");
        String username = formData.get("username");

        AppUser appUser = new AppUser(firstName, lastName, email, password, username);
        userserviceimpl.saveUser(appUser);
    }



    @PutMapping("/account/{userId}/updateAccountDetails")
    public ResponseEntity<AppUser> updateAccountDetails(
            @PathVariable Long userId,
            @RequestParam(required = false) String newFirstName,
            @RequestParam(required = false) String newLastName,
            @RequestParam(required = false) String newEmail,
            @RequestParam(required = false) String password
    ) {
        return userserviceimpl.updateAccountDetails(userId, newFirstName, newLastName, newEmail, password);
    }

    @GetMapping("/user")
    public ResponseEntity<AppUser> getUserData() {
        Long userId = 1L;
        AppUser user = appUserRepository.findById(userId).orElse(null);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<AppUser> findUserbyId(@PathVariable Long userId) {
        return userserviceimpl.findByAppUserID(userId);
    }

    @GetMapping("/message/{userId}/allMessages")
    public ResponseEntity<List<Message>> getAllMessages(@PathVariable Long userId) {
        return userserviceimpl.getAllMessages(userId);
    }

    @GetMapping("/message/{userId}/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long userId, @PathVariable Long messageId) {
        return userserviceimpl.getMessageById(userId, messageId);
    }

    @PostMapping("/message/{userId}/sendMessage")
    public ResponseEntity<Message> sendMessage(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> requestBody
    ) {
        String content = (String) requestBody.get("content");
        List<String> recipientNames = (List<String>) requestBody.get("recipientNames");

        ResponseEntity<Message> response = userserviceimpl.sendMessage(userId, content, recipientNames);

        if (response.getStatusCode() == HttpStatus.OK) {
            Message message = response.getBody();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("Location", "/message/" + message.getId())
                    .body(message);
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    @GetMapping("/message/{userId}/messagesByUsername/{username}")
    public ResponseEntity<List<Message>> getMessageByUser(@PathVariable Long userId, @PathVariable String username) {
        return userserviceimpl.getMessagesByUsername(userId, username);
    }

    @GetMapping("/post/{userId}/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long userId, @PathVariable Long postId) {
        return userserviceimpl.getPostById(userId, postId);
    }

    @PostMapping("/post/{userId}/createPost")
    public ResponseEntity<Post> createPost(
            @PathVariable Long userId,
            @RequestBody Post post
    ) {
        return userserviceimpl.createPost(userId, post);
    }

    @GetMapping("/post/{userId}/posts/{targetUserId}")
    public ResponseEntity<List<Post>> getAllPostsByUserId(@PathVariable Long userId, @PathVariable Long targetUserId) {
        return userserviceimpl.getPostsByUserId(userId, targetUserId);
    }

    @GetMapping("/post/{userId}/postsByUsername/{friendUsername}")
    public ResponseEntity<List<Post>> getAllPostsByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return userserviceimpl.getPostsByUsername(userId, friendUsername);
    }

    @GetMapping("/friendreqs/{userId}/getFriendRequests")
    public ResponseEntity<List<UserDTO>> getAllFriendRequests(@PathVariable Long userId) {
        System.out.println("REQUEST SENT!!!:" + userId);
        return userserviceimpl.getAllFriendRequestsDTOS(userId);
    }

    @PutMapping("/friendreqs/{userId}/sendFriendRequestById/{friendId}")
    public ResponseEntity<AppUser> sendFriendRequestById(@PathVariable Long userId, @PathVariable Long friendRequestRecipient) {
        return userserviceimpl.sendFriendRequest(userId, friendRequestRecipient);
    }

    @PutMapping("/friendreqs/{userId}/sendFriendRequestByUsername/{friendUsername}")
    public ResponseEntity<AppUser> sendFriendRequestByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return userserviceimpl.sendFriendRequest(userId, friendUsername);
    }

    @PutMapping("/friendreqs/{userId}/acceptFriendRequest/{potentialFriendId}")
    public ResponseEntity<AppUser> acceptFriendRequest(@PathVariable Long userId, @PathVariable Long potentialFriendId) {
        return userserviceimpl.acceptFriendRequest(userId, potentialFriendId);
    }

    @PutMapping("/friendreqs/{userId}/declineFriendRequest/{potentialFriendId}")
    public ResponseEntity<AppUser> declineFriendRequest(@PathVariable Long userId, @PathVariable Long potentialFriendId) {
        return userserviceimpl.declineFriendRequest(userId, potentialFriendId);
    }

    @GetMapping("/getAllWebsiteUsers")
    public ResponseEntity<List<AppUser>> GetAllWebsiteUsers() {
        return userserviceimpl.getAllUsers();
    }
}