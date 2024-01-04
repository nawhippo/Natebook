package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.CompressedImage;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.CommentRepository;
import SoloProject.SocialMediaApp.repository.PostRepository;
import SoloProject.SocialMediaApp.service.AccountService;
import SoloProject.SocialMediaApp.service.AppUserSearchService;
import SoloProject.SocialMediaApp.service.CompressionService;
import SoloProject.SocialMediaApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountservice;


    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppUserSearchService appUserSearchService;
    private final AppUserRepository appUserRepository;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final PostService postService;

    private final CompressionService compressionService;
    @Autowired
    public AccountController(CommentRepository commentRepository, PostRepository postRepository, AccountService userserviceimpl, AppUserRepository appUserRepository, PostService postService, CompressionService compressionService) {
        this.accountservice = userserviceimpl;
        this.appUserRepository = appUserRepository;
        this.postService = postService;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.compressionService = compressionService;
    }

    @GetMapping("/account/{userId}/accountDetails")
    public ResponseEntity<AppUser> getAccountDetails(@PathVariable Long userId) {
        return accountservice.getAccountDetails(userId);
    }
    @GetMapping("/account/{userId}/profilePicture")
    @Transactional
    public ResponseEntity<CompressedImage> getProfilePicture(@PathVariable Long userId) {
        CompressedImage profileImage = accountservice.getProfilePicture(userId);
        if (profileImage != null) {
            return ResponseEntity.ok(profileImage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/account/createAccount")
    public ResponseEntity<AppUser> createAccount(@RequestBody Map<String, String> formData) {
        //no need for checking validity, as that is in the front end
        return accountservice.createAccount(formData);
    }
    @PutMapping("/account/{userId}/ForgotPassword")
    public ResponseEntity<AppUser> ForgotPassword(@RequestBody Map<String, String> formData) {
        String email = formData.get("email");
        return accountservice.forgotPassword(email);
    }

    @PutMapping("/account/{userId}/updateAccountDetails")
    public ResponseEntity<AppUser> updateAccountDetails(@PathVariable Long userId, @RequestBody Map<String, String> formData) {
        String newFirstName = formData.get("firstname");
        String newLastName = formData.get("lastname");
        String newEmail = formData.get("email");
        String newPassword = formData.get("password");
        return accountservice.updateAccountDetails(userId, newFirstName, newLastName, newEmail, newPassword);
    }


    @GetMapping("/account/{userid}/getProfilePicture")
    @Transactional
public CompressedImage getProfilePictureFromUserId(@PathVariable Long userid){
        return accountservice.getProfilePicture(userid);
    }

    @PutMapping("/account/{userId}/uploadProfilePicture")
    @Transactional
    public Object uploadProfilePicture(@PathVariable Long userId, @RequestBody Map<String, String> formData) throws IOException {
        // Extract the Base64 image string from formData
        String base64Image = formData.get("image");

        if (base64Image == null || base64Image.isEmpty()) {
            return ResponseEntity.badRequest().body("No image data provided");
        }

        // Call the service method to handle the profile picture update
        return accountservice.uploadProfilePicture(userId, base64Image);
    }



    @DeleteMapping("/account/{userId}/deleteAccount")
    public ResponseEntity<AppUser> deleteAccount(@PathVariable Long userId){
       return accountservice.deleteAccount(userId);
    }
}
