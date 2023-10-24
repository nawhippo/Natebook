package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.CommentRepository;
import SoloProject.SocialMediaApp.repository.PostRepository;
import SoloProject.SocialMediaApp.service.AccountService;
import SoloProject.SocialMediaApp.service.AppUserSearchService;
import SoloProject.SocialMediaApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountservice;


    @Autowired
    private AppUserSearchService appUserSearchService;
    private final AppUserRepository appUserRepository;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final PostService postService;

    @Autowired
    public AccountController(CommentRepository commentRepository, PostRepository postRepository, AccountService userserviceimpl, AppUserRepository appUserRepository, PostService postService) {
        this.accountservice = userserviceimpl;
        this.appUserRepository = appUserRepository;
        this.postService = postService;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/account/{userId}/accountDetails")
    public ResponseEntity<AppUser> getAccountDetails(@PathVariable Long userId) {
        return accountservice.getAccountDetails(userId);
    }



    @PostMapping("/account/createAccount")
    public ResponseEntity<AppUser> createAccount(@RequestBody Map<String, String> formData) {
        //no need for checking validity, as that is in the front end
        String firstName = formData.get("firstname");
        String lastName = formData.get("lastname");
        String email = formData.get("email");
        String password = formData.get("password");
        String username = formData.get("username");

        AppUser appUser = new AppUser(firstName, lastName, email, password, username);
        accountservice.saveAccount(appUser);
        return ResponseEntity.ok(appUser);
    }
    @PutMapping("/account/{userId}/ForgotPassword")
    public ResponseEntity<AppUser> ForgotPassword(@PathVariable Long userId) {
        return accountservice.forgotPassword(userId);
    }

    @PutMapping("/account/{userId}/updateAccountDetails")
    public ResponseEntity<AppUser> updateAccountDetails(@PathVariable Long userId, @RequestBody Map<String, String> formData) {
        String newFirstName = formData.get("firstname");
        String newLastName = formData.get("lastname");
        String newEmail = formData.get("email");
        String newPassword = formData.get("password");
        return accountservice.updateAccountDetails(userId, newFirstName, newLastName, newEmail, newPassword);
    }

    //need more authorization for this.
    @DeleteMapping("/account/{userId}/deleteAccount")
    public ResponseEntity<AppUser> deleteAccount(@PathVariable Long userId){
       return accountservice.deleteAccount(userId);
    }
}
