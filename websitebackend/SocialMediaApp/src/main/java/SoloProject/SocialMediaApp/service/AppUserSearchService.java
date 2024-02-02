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

    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> allUsers = repository.findAll();
        return ResponseEntity.ok(allUsers);
    }
}