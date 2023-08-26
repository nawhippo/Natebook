package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.*;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;



@Service
public class AppUserService {

    private final AppUserRepository repository;



    public AppUserService(AppUserRepository repository, PasswordEncoder passwordEncoder, EntityManager entityManager) {
        this.repository = repository;
    }



    public AppUser createUser(String firstName, String lastName, String username, String email, String password) {
        if (repository.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username is already taken. Please choose a different username.");
        }
        List<Message> messages = new ArrayList<>();
        AppUser newuser = new AppUser(messages, firstName, lastName, username, email, password);
        repository.save(newuser);
        return newuser;
    }


    public ResponseEntity<AppUser> findByAppUserID(Long id) {
        AppUser appUser = repository.findByAppUserID(id);
        if (appUser != null) {
            return ResponseEntity.ok(appUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    //should only return 1 user

    public ResponseEntity<AppUser> findUser(Long id) {
        AppUser appUser = repository.findByAppUserID(id);
        if (appUser != null) {
            return ResponseEntity.ok(appUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //should only return 1 user

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
    private UserDTO convertToUserDTO(Long userId) {
        AppUser appUser = repository.findByAppUserID(userId);
        return new UserDTO(
                appUser.getAppUserID(),
                appUser.getUsername(),
                appUser.getFirstname(),
                appUser.getLastname(),
                appUser.getEmail()
        );
    }

    public ResponseEntity<AppUser> saveUser(AppUser appUser) {
        repository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
    }


}