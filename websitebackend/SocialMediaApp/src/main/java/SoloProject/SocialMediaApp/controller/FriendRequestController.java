package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.UserDTO;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;
    private final AppUserRepository appUserRepository;

    @Autowired
    public FriendRequestController(FriendRequestService friendRequestService, AppUserRepository appUserRepository){
        this.friendRequestService = friendRequestService;
        this.appUserRepository = appUserRepository;
    }






    @GetMapping("/friendreqs/{userId}/getFriendRequests")
    public ResponseEntity<List<UserDTO>> getAllFriendRequests(@PathVariable Long userId) {
        return friendRequestService.getAllFriendRequestsDTOS(userId);
    }

    @PutMapping("/friendreqs/{userId}/sendFriendRequestById/{friendId}")
    public ResponseEntity<AppUser> sendFriendRequestById(@PathVariable Long userId, @PathVariable Long friendRequestRecipient) {
        return friendRequestService.sendFriendRequest(userId, friendRequestRecipient);
    }

    @PutMapping("/friendreqs/{userId}/sendFriendRequestByUsername/{friendUsername}")
    public ResponseEntity<AppUser> sendFriendRequestByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return friendRequestService.sendFriendRequest(userId, friendUsername);
    }

    @PutMapping("/friendreqs/{userId}/acceptFriendRequest/{potentialFriendId}")
    public ResponseEntity<AppUser> acceptFriendRequest(@PathVariable Long userId, @PathVariable Long potentialFriendId) {
        return friendRequestService.acceptFriendRequest(userId, potentialFriendId);
    }

    @PutMapping("/friendreqs/{userId}/declineFriendRequest/{potentialFriendId}")
    public ResponseEntity<AppUser> declineFriendRequest(@PathVariable Long userId, @PathVariable Long potentialFriendId) {
        return friendRequestService.declineFriendRequest(userId, potentialFriendId);
    }
}
