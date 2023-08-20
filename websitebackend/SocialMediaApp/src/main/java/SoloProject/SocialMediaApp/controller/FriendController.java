package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.UserDTO;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FriendController {
    private final AppUserServiceImpl userserviceimpl;
    private final AppUserRepository appUserRepository;

    @Autowired
    public FriendController(AppUserServiceImpl userserviceimpl, AppUserRepository appUserRepository) {
        this.userserviceimpl = userserviceimpl;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/friends/{userId}/getAllFriends")
    public ResponseEntity<List<UserDTO>> getAllFriends(@PathVariable Long userId) {
        return userserviceimpl.getAllFriendsDTOS(userId);
    }

    @GetMapping("/friends/{userId}/{friendId}")
    public ResponseEntity<AppUser> getFriendById(@PathVariable Long userId, @PathVariable Long friendId) {
        return userserviceimpl.getFriend(userId, friendId);
    }

    @GetMapping("/friends/{userId}/{friendUsername}")
    public ResponseEntity<AppUser> getFriendByUsername(@PathVariable Long userId, @PathVariable String friendUsername) {
        return userserviceimpl.getFriend(userId, friendUsername);
    }

}
