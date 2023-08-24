package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class MessageController {
    private final AppUserServiceImpl userserviceimpl;
    private final AppUserRepository appUserRepository;

    @Autowired
    public MessageController(AppUserServiceImpl userserviceimpl, AppUserRepository appUserRepository) {
        this.userserviceimpl = userserviceimpl;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/message/{userId}/allMessages")
    public ResponseEntity<List<Message>> getAllMessages(@PathVariable Long userId) {
        return userserviceimpl.getAllMessages(userId);
    }

    @GetMapping("/message/{userId}/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long userId, @PathVariable Long messageId) {
        return userserviceimpl.getMessageById(userId, messageId);
    }

    @PostMapping("/message/{userId}/sendMessage")
    public ResponseEntity<Message> sendMessage(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> requestBody
    ) {
        String content = (String) requestBody.get("content");
        List<String> recipientNames = (List<String>) requestBody.get("recipientNames");

        ResponseEntity<Message> response = userserviceimpl.sendMessage(userId, content, recipientNames);

        if (response.getStatusCode() == HttpStatus.OK) {
            Message message = response.getBody();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("Location", "/message/" + message.getId())
                    .body(message);
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    @GetMapping("/message/{userId}/messagesByUsername/{username}")
    public ResponseEntity<List<Message>> getMessageByUser(@PathVariable Long userId, @PathVariable String username) {
        return userserviceimpl.getMessagesByUsername(userId, username);
    }



}
