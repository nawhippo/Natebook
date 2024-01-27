package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.ColorUtility;
import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.CompressedImage;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.CommentRepository;
import SoloProject.SocialMediaApp.repository.CompressedImageRepository;
import SoloProject.SocialMediaApp.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {
    private final AppUserRepository appUserRepository;
    private final CommentRepository commentRepository;
    private final CompressedImageRepository compressedImageRepository;
    private final PostRepository postRepository;
    private final EmailSenderService emailSenderService;
    private final CompressionService compressionService;

    private PasswordEncoder passwordEncoder;

    public AccountService(AppUserRepository appUserRepository,
                          CommentRepository commentRepository,
                          CompressedImageRepository compressedImageRepository, PostRepository postRepository,
                          EmailSenderService emailSenderService, CompressionService compressionService, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.commentRepository = commentRepository;
        this.compressedImageRepository = compressedImageRepository;
        this.postRepository = postRepository;
        this.emailSenderService = emailSenderService;
        this.compressionService = compressionService;
        this.passwordEncoder = passwordEncoder;
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
            return compressedImageRepository.findByProfileid(userId);
        }
        return null;
    }

    @Transactional
    public ResponseEntity<AppUser> updateAccountDetails(Long userId, String newFirstName, String newLastName, String newEmail, String newPassword, String newOccupation, String newBiography, Boolean newPrivate) {
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

        if(newOccupation != null){
            user.setOccupation(newOccupation);
        }

        if(newBiography != null){
            user.setBiography(newBiography);
        }

        if(newPrivate != null){
            user.setPrivate(newPrivate);
        }
        appUserRepository.save(user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<AppUser> saveAccount(AppUser appUser, String encodedPassword) {
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
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

    public ResponseEntity<?> uploadProfilePicture(Long userId, String base64Image) throws IOException {
        Optional<AppUser> optionalAppUser = appUserRepository.findById(userId);

        if (optionalAppUser.isPresent()) {
            AppUser appUser = optionalAppUser.get();
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            String filename = "image_" + System.currentTimeMillis() + ".jpg";
            CompressedImage compressedImage = compressionService.compressImage(imageBytes, filename);

            appUser.setProfilePicture(compressedImage.getId());
            compressedImage.setProfileid(appUser.getAppUserID());
            appUserRepository.save(appUser);
            compressedImageRepository.save(compressedImage);
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<AppUser> createAccount(Map<String, String> formData) {
        String firstName = formData.get("firstname");
        String lastName = formData.get("lastname");
        String email = formData.get("email");
        String password = formData.get("password");
        String username = formData.get("username");

        String encodedPassword = passwordEncoder.encode(password);
        AppUser appUser = new AppUser(firstName, lastName, email, encodedPassword, username);
        appUser.addAuthority("ROLE_USER");
        appUser.setFriendCount(0);
        String randomColor = ColorUtility.getRandomColor();
        appUser.setProfileColor(randomColor);
        appUserRepository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
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

    public boolean checkAuthenticationMatch(String username, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String authenticatedUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
            System.out.println("USERNAME:  " + authenticatedUsername);
            return username.equals(authenticatedUsername);
        }
        return false;
    }

}
