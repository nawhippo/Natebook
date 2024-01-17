package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowService {
    @Autowired
    AppUserRepository appUserRepository;

    public ResponseEntity<?> followUser(Long userid, Long followedid){
        AppUser appUser = appUserRepository.findByAppUserID(userid);
        AppUser appUser1 = appUserRepository.findByAppUserID(followedid);
        if(!appUser1.getBlockList().contains(appUser.getId())) {
            appUser.getFollowing().add(appUser1.getId());
            appUser1.getFollowers().add(appUser.getId());
            appUserRepository.save(appUser);
            appUserRepository.save(appUser1);
            return ResponseEntity.ok(appUser);
        }
        return (ResponseEntity<?>) ResponseEntity.badRequest();
    }


    public ResponseEntity<?> unfollowUser(Long userId, Long followedId) {
        AppUser user = appUserRepository.findByAppUserID(userId);
        AppUser followedUser = appUserRepository.findByAppUserID(followedId);

        if (user == null || followedUser == null) {
            return ResponseEntity.badRequest().body("Invalid user ID(s)");
        }

        if (user.getFollowing().contains(followedId) && followedUser.getFollowers().contains(userId)) {
            user.getFollowing().remove(followedId);
            followedUser.getFollowers().remove(userId);
            appUserRepository.save(user);
            appUserRepository.save(followedUser);

            return ResponseEntity.ok("Unfollowed successfully");
        } else {
            return ResponseEntity.badRequest().body("User not following");
        }
    }

    public ResponseEntity<List<AppUser>> getFollowers(Long userId) {
        AppUser user = appUserRepository.findByAppUserID(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }

        List<AppUser> followers = new ArrayList<>();
        for (Long followerId : user.getFollowers()) {
            AppUser follower = appUserRepository.findByAppUserID(followerId);
            if (follower != null) {
                followers.add(follower);
            }
        }
        return ResponseEntity.ok(followers);
    }

    public ResponseEntity<List<AppUser>> getFollowing(Long userId) {
        AppUser user = appUserRepository.findByAppUserID(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }

        List<AppUser> following = new ArrayList<>();
        for (Long followingId : user.getFollowing()) {
            AppUser followedUser = appUserRepository.findByAppUserID(followingId);
            if (followedUser != null) {
                following.add(followedUser);
            }
        }
        return ResponseEntity.ok(following);
    }
}

