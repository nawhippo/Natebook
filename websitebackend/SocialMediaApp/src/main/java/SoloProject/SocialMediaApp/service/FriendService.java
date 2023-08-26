package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.UserDTO;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class FriendService {
    private final AppUserRepository repository;

    public FriendService(AppUserRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<AppUser> removeFriend(Long userId, String username) {
        AppUser user = repository.findByAppUserID(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<Long> friends = user.getFriends();
        AppUser friend = repository.findByUsername(username);

        if (friend == null) {
            return ResponseEntity.notFound().build();
        }
        friends.remove(friend);
        user.setFriends(friends);
        repository.save(user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<List<Long>> getFriends(Long userId) {
        ResponseEntity<AppUser> response = findByAppUserID(userId);

        if (response != null && response.getBody() != null) {
            AppUser appUser = response.getBody();
            return ResponseEntity.ok(appUser.getFriends());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<AppUser> getFriend(Long userId, Long friendId) {
        AppUser appUser = findByAppUserID(userId).getBody();
        if (appUser == null) {
            return ResponseEntity.notFound().build();
        } else {
            for (Long friend : appUser.getFriends()) {
                if (friend == friendId) {
                    return findByAppUserID(friend);
                }
            }
            return ResponseEntity.notFound().build();
        }
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











    public ResponseEntity<List<UserDTO>> getAllFriendsDTOS(Long UserId) {
        AppUser user = repository.findByAppUserID(UserId);
        if (user != null && user.getFriends() != null) {
            List<Long> friends = user.getFriends();
            List<UserDTO> DTOList = convertToDTOList(friends);
            return ResponseEntity.ok(DTOList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    private List<UserDTO> convertToDTOList(List<Long> ids) {
        List<UserDTO> result = new ArrayList<>();
        if (ids != null) {
            for (long id : ids) {
                result.add(convertToDTO(id));
            }
        }
        return result;
    }

    private UserDTO convertToDTO(Long id) {
        AppUser user = repository.findByAppUserID(id);
        if (user != null) {
            return new UserDTO(
                    id, user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail()
            );
        }
        return null;
    }
































}
