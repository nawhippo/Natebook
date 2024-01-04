package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.AppUserDTO;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserSearchService {

    private final AppUserRepository repository;



    public AppUserSearchService(AppUserRepository repository) {
        this.repository = repository;
    }





    public ResponseEntity<AppUser> createUser(String firstName, String lastName, String username, String email, String password) {
        if (repository.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username is already taken. Please choose a different username.");
        }
        AppUser newuser = new AppUser(firstName, lastName, username, email, password);
        repository.save(newuser);
        return ResponseEntity.ok(newuser);
    }


    public ResponseEntity<AppUser> blockUser(Long userId, Long blockId){
        AppUser user = repository.findByAppUserID(userId);

        user.getBlockList().add(blockId);
        repository.save(user);
        return ResponseEntity.ok(user);
    }
    public ResponseEntity<AppUser> findByAppUserID(Long id) {
        AppUser appUser = repository.findByAppUserID(id);
        if (appUser != null) {
            return ResponseEntity.ok(appUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<AppUser> findUser(Long id) {
        AppUser appUser = repository.findByAppUserID(id);
        if (appUser != null) {
            return ResponseEntity.ok(appUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<AppUser> findUser(String username) {
        AppUser appUser = repository.findByUsername(username);
        if (appUser != null) {
            return ResponseEntity.ok(appUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<List<AppUser>> findRelatedUsers(String firstname, String lastname) {
        List<AppUser> appUsers;

        if (firstname != null && lastname != null) {
            appUsers = repository.findByFirstnameAndLastname(firstname, lastname);
        } else if (firstname != null) {
            appUsers = repository.findByFirstname(firstname);
        } else if (lastname != null) {
            appUsers = repository.findByLastname(lastname);
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(appUsers);
    }
    public ResponseEntity<List<Long>> getAllFriendRequests(Long UserId) {
        AppUser user = repository.findByAppUserID(UserId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.getRequests());
    }

    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> allUsers = repository.findAll();
        return ResponseEntity.ok(allUsers);
    }
    //to display user data extrapolated from longs.
    private AppUserDTO convertToUserDTO(Long userId) {
        AppUser appUser = repository.findByAppUserID(userId);
        return new AppUserDTO(
                appUser
        );
    }
}