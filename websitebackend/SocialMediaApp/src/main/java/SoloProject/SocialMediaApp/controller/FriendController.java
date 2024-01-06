package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AccountService;
import SoloProject.SocialMediaApp.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FriendController {
    private final FriendService friendService;
    private final AppUserRepository appUserRepository;
    private final AccountService accountService;

    @Autowired
    public FriendController(FriendService friendService, AppUserRepository appUserRepository,
                            AccountService accountService) {
        this.friendService = friendService;
        this.appUserRepository = appUserRepository;
        this.accountService = accountService;
    }

    @DeleteMapping("/friends/{userId}/removeFriend/{friendusername}")
    public ResponseEntity<?> removeFriend(@PathVariable Long userId,
                                                @PathVariable String friendusername,
                                                Authentication authentication) {
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return friendService.removeFriend(userId, friendusername);
    }

    @GetMapping("/friends/{userId}/getAllFriends")
    public ResponseEntity<?> getAllFriends(@PathVariable Long userId,
                                                       Authentication authentication) {
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return friendService.getAllFriendsAppUsers(userId);
    }

    @GetMapping("/friends/{userId}/{friendId}")
    public ResponseEntity<?> getFriendById(@PathVariable Long userId,
                                                 @PathVariable Long friendId,
                                                 Authentication authentication) {
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return friendService.getFriend(userId, friendId);
    }

    @GetMapping("/friends/{userId}/{friendUsername}")
    public ResponseEntity<?> getFriendByUsername(@PathVariable Long userId,
                                                       @PathVariable String friendUsername,
                                                       Authentication authentication) {
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return friendService.getFriend(userId, friendUsername);
    }
}