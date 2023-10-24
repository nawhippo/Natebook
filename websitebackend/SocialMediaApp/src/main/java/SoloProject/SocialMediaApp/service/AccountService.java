package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.CommentRepository;
import SoloProject.SocialMediaApp.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private final AppUserRepository appUserRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final EmailSenderService emailSenderService;

    public AccountService(AppUserRepository appUserRepository, CommentRepository commentRepository, PostRepository postRepository, EmailSenderService emailSenderService) {
        this.appUserRepository = appUserRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.emailSenderService = emailSenderService;
    }

    public ResponseEntity<AppUser> getAccountDetails(Long userid) {
        AppUser user = appUserRepository.findByAppUserID(userid);
        return ResponseEntity.ok(user);
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

    public ResponseEntity<AppUser> saveAccount(AppUser appUser) {
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


    public ResponseEntity<?> createAccountFromGoogle(String firstName, String lastName, String email) {
        String username = email.split("@")[0];
        String password = "password";
        AppUser user = new AppUser(firstName, lastName, email, password, username);
        appUserRepository.save(user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<AppUser> forgotPassword(Long userId) {
        AppUser user = appUserRepository.findByAppUserID(userId);
        String email = user.getEmail();
        return emailSenderService.sendEmail(email);
    }
}
