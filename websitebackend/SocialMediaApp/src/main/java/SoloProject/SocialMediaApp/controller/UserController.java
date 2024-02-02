package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.*;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AccountService;
import SoloProject.SocialMediaApp.service.AppUserSearchService;
import SoloProject.SocialMediaApp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private final AppUserSearchService appUserSearchService;
    private final AppUserRepository appUserRepository;

    private final PostService postService;
    private final AccountService accountService;

    @Autowired
    public UserController(AppUserSearchService appUserSearchService, AppUserRepository appUserRepository, PostService postService, AccountService accountService) {
        this.appUserSearchService = appUserSearchService;
        this.appUserRepository = appUserRepository;
        this.postService = postService;
        this.accountService = accountService;
    }

    @GetMapping
    public String helloWorld() {
        return "Hello, user!";
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findUserById(@PathVariable Long userId) {
        ResponseEntity<AppUser> response = appUserSearchService.findByAppUserID(userId);

        if (response.getBody() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + userId + " not found");
        }

        AppUser user = response.getBody();
        AppUserDTO appuserDTO = new AppUserDTO(user);
        return ResponseEntity.ok(appuserDTO);
    }

//    @GetMapping("/user/{username}")
//    public ResponseEntity<AppUser> findByUsername(@PathVariable String username) {
//        return appUserService.findUser(username);
//    }

    @GetMapping("/user/getAllWebsiteUsers")
    public ResponseEntity<List<AppUser>> GetAllWebsiteUsers() {
        return appUserSearchService.getAllUsers();
    }



    @PutMapping("/user/block/{userId}/{blockId}")
    public ResponseEntity<?> BlockUser(@PathVariable Long userId, @PathVariable Long blockId, Authentication authentication) {
        Optional<AppUser> appUserOptional = appUserRepository.findById(userId);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            if (accountService.checkAuthenticationMatch(appUser.getUsername(), authentication)) {
                return appUserSearchService.blockUser(userId, blockId);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("user/{userid}/{profileUserId}")
    public ResponseEntity<List<Post>> displayAllUserPosts(@PathVariable Long userid, @PathVariable Long profileUserId) {
        List<Post> posts = postService.getPostsByPosterId(userid, profileUserId);
        if (posts == null || posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
    }


    @GetMapping("/checkExistence/{userName}")
    public ResponseEntity<Long> checkUserExistence(@PathVariable String userName) {
        AppUser appUser = appUserRepository.findByUsername(userName);
        if (appUser != null) {
            return ResponseEntity.ok(appUser.getAppUserID());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}