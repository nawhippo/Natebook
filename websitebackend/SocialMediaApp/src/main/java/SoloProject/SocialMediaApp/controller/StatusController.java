package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.StatusCreationRequest;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AccountService;
import SoloProject.SocialMediaApp.service.StatusService;
import SoloProject.SocialMediaApp.models.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AppUserRepository appUserRepository;


    @PostMapping("/status/create")
    public ResponseEntity<?> createStatus(@RequestParam Long appUserId,
                                          @RequestBody StatusCreationRequest request,
                                          Authentication authentication)  {
        String content = request.getContent();
        int lifespan = request.getLifespan();
        Status newStatus = statusService.createStatus(appUserId, content, lifespan);
        AppUser appUser = appUserRepository.findByAppUserID(appUserId);
        String username = appUser.getUsername();
        if (!accountService.checkAuthenticationMatch(username,authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        return ResponseEntity.ok(newStatus);
    }

    @GetMapping("/status/getByUser/{appUserId}")
    public ResponseEntity<?> getStatusByUser(@PathVariable Long appUserId) {
        Status status = statusService.getStatusByUser(appUserId);
        if (status == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No status found for user with ID: " + appUserId);
        }

        return ResponseEntity.ok(status);
    }
}