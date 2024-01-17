package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FollowController {
    @Autowired
    private FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<?> followUser(@RequestParam Long userId, @RequestParam Long followedId) {
        return followService.followUser(userId, followedId);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> unfollowUser(@RequestParam Long userId, @RequestParam Long followedId) {
        return followService.unfollowUser(userId, followedId);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<AppUser>> getFollowers(@PathVariable Long userId) {
        return followService.getFollowers(userId);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<AppUser>> getFollowing(@PathVariable Long userId) {
        return followService.getFollowing(userId);
    }
}
