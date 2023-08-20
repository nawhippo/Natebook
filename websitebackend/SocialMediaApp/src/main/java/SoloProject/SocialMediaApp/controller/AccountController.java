package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api")
public class AccountController {

    private final AppUserServiceImpl userserviceimpl;
    private final AppUserRepository appUserRepository;

    @Autowired
    public AccountController(AppUserServiceImpl userserviceimpl, AppUserRepository appUserRepository) {
        this.userserviceimpl = userserviceimpl;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/account/{userId}/accountDetails")
    public ResponseEntity<AppUser> getAccountDetails(@PathVariable Long userId) {
        return userserviceimpl.getAccountDetails(userId);
    }

    @PostMapping("/account/createAccount")
    public void createAccount(@RequestBody Map<String, String> formData) {
        String firstName = formData.get("firstname");
        String lastName = formData.get("lastname");
        String email = formData.get("email");
        String password = formData.get("password");
        String username = formData.get("username");

        AppUser appUser = new AppUser(firstName, lastName, email, password, username);
        userserviceimpl.saveUser(appUser);
    }



    @PutMapping("/account/{userId}/updateAccountDetails")
    public ResponseEntity<AppUser> updateAccountDetails(
            @PathVariable Long userId,
            @RequestParam(required = false) String newFirstName,
            @RequestParam(required = false) String newLastName,
            @RequestParam(required = false) String newEmail,
            @RequestParam(required = false) String password
    ) {
        return userserviceimpl.updateAccountDetails(userId, newFirstName, newLastName, newEmail, password);
    }

}
