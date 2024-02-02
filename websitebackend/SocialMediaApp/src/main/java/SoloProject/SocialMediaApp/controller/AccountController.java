package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.CompressedImage;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.CommentRepository;
import SoloProject.SocialMediaApp.repository.PostRepository;
import SoloProject.SocialMediaApp.service.AccountService;
import SoloProject.SocialMediaApp.service.CompressionService;
import SoloProject.SocialMediaApp.service.PostService;
import SoloProject.SocialMediaApp.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;
    private final AppUserRepository appUserRepository;

    private final VerificationService verificationService;

    @Autowired
    public AccountController(CommentRepository commentRepository, PostRepository postRepository,
                             AccountService userserviceimpl, AppUserRepository appUserRepository,
                             PostService postService, CompressionService compressionService, VerificationService verificationService) {
        this.accountService = userserviceimpl;
        this.appUserRepository = appUserRepository;
        this.verificationService = verificationService;
    }

    @GetMapping("/account/{userId}/accountDetails")
    public ResponseEntity<AppUser> getAccountDetails(@PathVariable Long userId) {
        return accountService.getAccountDetails(userId);
    }

    @GetMapping("/account/{userId}/profilePicture")
    @Transactional
    public ResponseEntity<CompressedImage> getProfilePicture(@PathVariable Long userId) {
        CompressedImage profileImage = accountService.getProfilePicture(userId);
        if (profileImage != null) {
            return ResponseEntity.ok(profileImage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/account/createAccount")
    public ResponseEntity<?> createAccount(@RequestBody Map<String, String> formData) {
        return accountService.createAccount(formData);
    }

    @PutMapping("/account/ForgotPassword")
    public ResponseEntity<AppUser> forgotPassword(@RequestBody Map<String, String> formData) {
        String email = formData.get("email");
        return accountService.forgotPassword(email);
    }

    @PutMapping("/account/{userId}/updateAccountDetails")
    public ResponseEntity<?> updateAccountDetails(@PathVariable Long userId,
                                                        @RequestBody Map<String, String> formData,
                                                        Authentication authentication) {
        String username = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(username, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        String newFirstName = formData.get("firstname");
        String newLastName = formData.get("lastname");
        String newEmail = formData.get("email");
        String newPassword = formData.get("password");
        String newOccupation = formData.get("occupation");
        String newBiography = formData.get("biography");
        Boolean newIsPrivate = Boolean.parseBoolean(formData.get("isPrivate"));
        return accountService.updateAccountDetails(userId, newFirstName, newLastName, newEmail, newPassword, newOccupation, newBiography, newIsPrivate);
    }

    @PostMapping("/account/generateVerificationToken")
    public ResponseEntity<String> generateVerificationToken(@RequestBody String email) {
        String verificationToken = verificationService.createAndSendEmailToken(email);
    }


    @GetMapping("/account/{userid}/getProfilePicture")
    @Transactional
    public CompressedImage getProfilePictureFromUserId(@PathVariable Long userid){
        return accountService.getProfilePicture(userid);
    }

    @PutMapping("/account/{userId}/uploadProfilePicture")
    @Transactional
    public ResponseEntity<?> uploadProfilePicture(@PathVariable Long userId,
                                                  @RequestBody Map<String, String> formData,
                                                  Authentication authentication) throws IOException {
        String username = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(username, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        String base64Image = formData.get("image");
        if (base64Image == null || base64Image.isEmpty()) {
            return ResponseEntity.badRequest().body("No image data provided");
        }
        return accountService.uploadProfilePicture(userId, base64Image);
    }

    @DeleteMapping("/account/{userId}/deleteAccount")
    public ResponseEntity<?> deleteAccount(@PathVariable Long userId,
                                                 Authentication authentication) {
        String username = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(username, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return accountService.deleteAccount(userId);
    }
}