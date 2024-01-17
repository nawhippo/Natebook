package SoloProject.SocialMediaApp.controller;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AccountService;
import SoloProject.SocialMediaApp.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;
    private final AppUserRepository appUserRepository;
    private final AccountService accountService;

    @Autowired
    public FriendRequestController(FriendRequestService friendRequestService,
                                   AppUserRepository appUserRepository,
                                   AccountService accountService) {
        this.friendRequestService = friendRequestService;
        this.appUserRepository = appUserRepository;
        this.accountService = accountService;
    }

    @GetMapping("/friendreqs/{userId}/getFriendRequests")
    public ResponseEntity<?> getAllFriendRequests(@PathVariable Long userId,
                                                                 Authentication authentication) {
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return friendRequestService.getAllFriendRequestsDTOS(userId);
    }

    @PutMapping("/friendreqs/{userId}/sendFriendRequestById/{friendId}")
    public ResponseEntity<?> sendFriendRequestById(@PathVariable Long userId,
                                                         @PathVariable Long friendId,
                                                         Authentication authentication) {
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return friendRequestService.sendFriendRequest(userId, friendId);
    }

    @PutMapping("/friendreqs/{userId}/sendFriendRequestByUsername/{friendUsername}")
    public ResponseEntity<?> sendFriendRequestByUsername(@PathVariable Long userId,
                                                               @PathVariable String friendUsername,
                                                               Authentication authentication) {
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return friendRequestService.sendFriendRequest(userId, friendUsername);
    }

    @PutMapping("/friendreqs/{userId}/acceptFriendRequest/{potentialFriendId}")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long userId,
                                                       @PathVariable Long potentialFriendId,
                                                       Authentication authentication) {
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return friendRequestService.acceptFriendRequest(userId, potentialFriendId);
    }

    @PutMapping("/friendreqs/{userId}/declineFriendRequest/{potentialFriendId}")
    public ResponseEntity<?> declineFriendRequest(@PathVariable Long userId,
                                                        @PathVariable Long potentialFriendId,
                                                        Authentication authentication) {
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return friendRequestService.declineFriendRequest(userId, potentialFriendId);
    }


    @GetMapping("/notifications/{userId}")
    public ResponseEntity<?> getAllNotifications(@PathVariable Long userId, Authentication authentication) {
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return friendRequestService.getAllFriendRequestNotifications(userId);
    }


}