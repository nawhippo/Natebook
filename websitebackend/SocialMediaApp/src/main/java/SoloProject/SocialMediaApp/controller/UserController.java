package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.models.UserDTO;
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
    private AppUserServiceImpl userserviceimpl;
    @Autowired
    public UserController(AppUserServiceImpl userserviceimpl) {
        this.userserviceimpl = userserviceimpl;
    }
    private AppUserRepository appUserRepository;

    @GetMapping("/{userId}/allFriends")
    public ResponseEntity<List<UserDTO>> getAllFriends(@PathVariable Long userId){
        return userserviceimpl.getAllFriendsDTOS(userId);
    }



    @GetMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<AppUser> getFriendById(@PathVariable Long userId, @PathVariable Long friendId) {
        return userserviceimpl.getFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/{friendUsername}")
    public ResponseEntity<AppUser> getFriendByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return userserviceimpl.getFriend(userId, friendUsername);
    }




    @GetMapping("/{userId}/accountDetails")
    public ResponseEntity<AppUser> getAccountDetails(@PathVariable Long userId){
        return userserviceimpl.getAccountDetails(userId);
    }

    @PutMapping("/{userId}/updateAccountDetails")
    public ResponseEntity<AppUser> updateAccountDetails(
            @PathVariable Long userId,
            @RequestParam(required = false) String newFirstName,
            @RequestParam(required = false) String newLastName,
            @RequestParam(required = false) String newEmail
    ) {
        return userserviceimpl.updateAccountDetails(userId, newFirstName, newLastName, newEmail);
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

//    @GetMapping("/{userId}/postsByUsername/{username}")
//    public ResponseEntity<List<Post>> getAllPosts(@PathVariable Long userId, @PathVariable String username) {
//        return userserviceimpl.getPostsByUsername(userId, username);
//    }
//


    @GetMapping("/{userId}/messagesByUsername/{username}")
    public ResponseEntity<List<Message>> getAllMessages(@PathVariable Long userId, @PathVariable String username) {
        return userserviceimpl.getMessagesByUsername(userId, username);
    }

    @GetMapping("/{userId}/getFriendRequests/")
    public ResponseEntity<List<UserDTO>> getAllFriendRequests(@PathVariable Long userId) {
        return userserviceimpl.getAllFriendRequestsDTOS(userId);
    }


    @PostMapping("/{userId}/createPost")
    public ResponseEntity<Post> createPost(
            @PathVariable Long userId,
            @RequestBody Post post
    ) {
        return userserviceimpl.createPost(userId, post);
    }

    @PutMapping("/{userId}/sendFriendRequestById/{friendId}")
    public ResponseEntity<AppUser> sendFriendRequestById(@PathVariable Long userId, @PathVariable Long friendRequestRecipient){
        return userserviceimpl.sendFriendRequest(userId, friendRequestRecipient);
    }

    @PutMapping("/{userId}/sendFriendRequestByUsername/{friendUsername}")
    public ResponseEntity<AppUser> sendFriendRequestByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return userserviceimpl.sendFriendRequest(userId, friendUsername);
    }

    @PutMapping("/{userId}/acceptFriendRequest/{potentialFriendId}")
    public ResponseEntity<AppUser> acceptFriendRequest(@PathVariable Long userId, @PathVariable Long potentialFriendId) {
        return userserviceimpl.acceptFriendRequest(userId, potentialFriendId);
    }

    @PutMapping("/{userId}/declineFriendRequest/{potentialFriendId}")
    public ResponseEntity<AppUser> declineFriendRequest(@PathVariable Long userId, @PathVariable Long potentialFriendId) {
        return userserviceimpl.declineFriendRequest(userId, potentialFriendId);
    }


    @GetMapping("/{userId}/posts/{targetUserId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId, @PathVariable Long targetUserId) {
        return userserviceimpl.getPostsByUserId(userId, targetUserId);
    }

    @GetMapping("/{userId}/postsByUsername/{friendUsername}")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return userserviceimpl.getPostsByUsername(userId, friendUsername);
    }

    @GetMapping("/getAllWebsiteUsers")
    public ResponseEntity<List<AppUser>> GetAllWebsiteUsers() {
    return userserviceimpl.getAllUsers();
}
}