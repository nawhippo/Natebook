package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.UserDTO;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AppUserService;
import SoloProject.SocialMediaApp.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FriendController {
    private final FriendService friendService;
    private final AppUserRepository appUserRepository;

    @Autowired
    public FriendController(FriendService friendService, AppUserRepository appUserRepository) {
        this.friendService = friendService;
        this.appUserRepository = appUserRepository;
    }

    @DeleteMapping("/friends/{userId}/removeFriend/{friendusername}")
        public ResponseEntity<AppUser> removeFriend(@PathVariable Long userId, @PathVariable String friendusername) {
            return friendService.removeFriend(userId, friendusername);
        }


    @GetMapping("/friends/{userId}/getAllFriends")
    public ResponseEntity<List<Long>> getAllFriends(@PathVariable Long userId) {
        return friendService.getAllFriends(userId);
    }

    @GetMapping("/friends/{userId}/{friendId}")
    public ResponseEntity<AppUser> getFriendById(@PathVariable Long userId, @PathVariable Long friendId) {
        return friendService.getFriend(userId, friendId);
    }

    @GetMapping("/friends/{userId}/{friendUsername}")
    public ResponseEntity<AppUser> getFriendByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return friendService.getFriend(userId, friendUsername);
    }

}
