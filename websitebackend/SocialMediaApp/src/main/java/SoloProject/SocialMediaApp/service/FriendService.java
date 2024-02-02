package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.AppUserDTO;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FriendService {
    private final AppUserRepository repository;

    public FriendService(AppUserRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<AppUser> removeFriend(Long userId, String friendUsername) {
        AppUser user = repository.findByAppUserID(userId);
        AppUser friend = repository.findByUsername(friendUsername);

        if (user != null && friend != null) {
            user.getFriends().remove(friend.getAppUserID());
            user.setFriendCount(user.getFriendCount() - 1);

            friend.getFriends().remove(userId);
            friend.setFriendCount(friend.getFriendCount() - 1);

            repository.save(user);
            repository.save(friend);

            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<AppUser> getFriend(Long userId, Long friendId) {
        AppUser appUser = repository.findByAppUserID(userId);
        if(appUser != null){
           List<Long> friends = appUser.getFriends();
            //check the user's friendlist.
           for(Long friend : friends) {
               if (friend == friendId) {
                   AppUser returnfriend = repository.findByAppUserID(friend);
                   return ResponseEntity.ok(returnfriend);
               }
           }
        }
        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<AppUser> getFriend(Long userId, String friendUsername) {
        AppUser user = repository.findByAppUserID(userId);
        for (Long friend : user.getFriends()) {
            if (repository.findByAppUserID(friend).getUsername().equals(friendUsername)) {
                return ResponseEntity.ok(repository.findByUsername(friendUsername));
            }
        }
        return ResponseEntity.notFound().build();
    }




    public ResponseEntity<List<AppUser>> getAllFriendsAppUsers(Long userId) {
        AppUser user = repository.findByAppUserID(userId);


        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<AppUser> friends = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            AppUser friend = repository.findByAppUserID(friendId);
            friends.add(friend);
        }

        return ResponseEntity.ok(friends);
    }
}
