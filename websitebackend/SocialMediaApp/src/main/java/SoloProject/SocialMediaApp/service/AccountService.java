package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.CompressedImage;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.CommentRepository;
import SoloProject.SocialMediaApp.repository.CompressedImageRepository;
import SoloProject.SocialMediaApp.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AppUserRepository appUserRepository;
    private final CommentRepository commentRepository;
    private final CompressedImageRepository compressedImageRepository;
    private final PostRepository postRepository;
    private final EmailSenderService emailSenderService;
    private final CompressionService compressionService;

    public AccountService(AppUserRepository appUserRepository,
                          CommentRepository commentRepository,
                          CompressedImageRepository compressedImageRepository, PostRepository postRepository,
                          EmailSenderService emailSenderService, CompressionService compressionService) {
        this.appUserRepository = appUserRepository;
        this.commentRepository = commentRepository;
        this.compressedImageRepository = compressedImageRepository;
        this.postRepository = postRepository;
        this.emailSenderService = emailSenderService;
        this.compressionService = compressionService;
    }

    public void addProfilePicture(Long userId, String base64Image) throws IOException {
        AppUser appUser = appUserRepository.findByAppUserID(userId);
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        String filename = "profile_" + userId + ".jpg"; // Unique filename for each user
        CompressedImage profileImage = compressionService.compressImage(imageBytes, filename);
        appUser.setProfilePicture(profileImage.getId());
        appUserRepository.save(appUser);
    }

    public ResponseEntity<AppUser> getAccountDetails(Long userid) {
        AppUser user = appUserRepository.findByAppUserID(userid);
        return ResponseEntity.ok(user);
    }

    public AppUser getAccountDetails(String username) {
        AppUser user = appUserRepository.findByUsername(username);
        return user;
    }

    public CompressedImage getProfilePicture(Long userId) {
        AppUser user = appUserRepository.findByAppUserID(userId);
        if (user != null && user.getProfilePicture() != null) {
            return compressedImageRepository.findById(user.getProfilePicture()).orElse(null);
        }
        return null;
    }

    @Transactional
    public ResponseEntity<AppUser> updateAccountDetails(Long userId, String newFirstName, String newLastName, String newEmail, String newPassword) {
        AppUser user = appUserRepository.findByAppUserID(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (newFirstName != null) {
            user.setFirstname(newFirstName);
        }
        if (newLastName != null) {
            user.setLastname(newLastName);
        }
        if (newEmail != null) {
            user.setEmail(newEmail);
        }

        if (newPassword != null) {
            user.setPassword(newPassword);
        }
        appUserRepository.save(user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<AppUser> saveAccount(AppUser appUser, String encodedPassword) {
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
    }
    public ResponseEntity<AppUser> createAccountFromUser(OAuth2User principal){
        String firstName = principal.getAttribute("given_name");
        String lastName = principal.getAttribute("family_name");
        String email = principal.getAttribute("email");
        //username is email removed from it.
        String username = email.split("@")[0];
        //username must be added as a parameter
        AppUser existingUser = appUserRepository.findByUsername(username);

        if (existingUser != null) {
            // User already exists, update if needed
            return ResponseEntity.ok(existingUser);
        }
        //merely a placeholder, then again, how will users sign back in again if they've already logged in with google
        String password = "defaultPassword";

        AppUser newUser = new AppUser(firstName, lastName, username, email, password);
        appUserRepository.save(newUser);
        return ResponseEntity.ok(newUser);
    }

    //anyone can make a request to this endpoint, potential security concern
    @Transactional
    public ResponseEntity<AppUser> deleteAccount(Long userId) {
        AppUser user = appUserRepository.findByAppUserID(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        for (Long friend : user.getFriends()) {
            AppUser friendUser = appUserRepository.findByAppUserID(friend);
            friendUser.getFriends().remove(userId);
            appUserRepository.save(friendUser);
        }

        commentRepository.deleteByCommenterId(userId);
        postRepository.deleteByPosterId(userId);
        appUserRepository.delete(user);

        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> uploadProfilePicture(Long userId, String base64Image) {
        Optional<AppUser> userOptional = appUserRepository.findById(userId);

        if (userOptional.isPresent() && base64Image != null && !base64Image.isEmpty()) {
            AppUser user = userOptional.get();
            try {
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                String filename = "profile_" + userId + "_" + System.currentTimeMillis() + ".jpg"; // Generate a filename
                CompressedImage compressedImage = compressionService.compressImage(imageBytes, filename);

                // Assuming you have a method to set the profile picture in your AppUser model
                user.setProfilePicture(compressedImage.getId());
                appUserRepository.save(user);

                return ResponseEntity.ok().body("Profile picture updated successfully");
            } catch (IOException e) {
                // Handle any exceptions
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing image");
            }
        }

        return ResponseEntity.notFound().build();
    }
    public ResponseEntity<?> createAccountFromGoogle(String firstName, String lastName, String email) {
        String username = email.split("@")[0];
        String password = "password";
        AppUser user = new AppUser(firstName, lastName, email, password, username);
        appUserRepository.save(user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<AppUser> forgotPassword(String email) {
        AppUser user = appUserRepository.findByEmail(email);
        emailSenderService.sendEmail(email);
        return ResponseEntity.ok(user);
    }


}
