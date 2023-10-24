package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class MessageController {
    private final MessageService messageService;
    private final AppUserRepository appUserRepository;

    @Autowired
    public MessageController(MessageService messageService, AppUserRepository appUserRepository) {
        this.messageService = messageService;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/message/{userId}/receivedMessages")
    public ResponseEntity<List<Message>> getAllRecMessages(@PathVariable Long userId) {
        if(messageService.getAllRecMessages(userId) == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(messageService.getAllRecMessages(userId));
    }

    @GetMapping("/message/{userId}/sentMessages")
    public ResponseEntity<List<Message>> getAllSentMessages(@PathVariable Long userId) {
        if(messageService.getAllSentMessages(userId) == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(messageService.getAllSentMessages(userId));
    }


    @PostMapping("/message/{userId}/sendMessage")
    public ResponseEntity<Message> sendMessage(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> requestBody
    ) {
        String content = (String) requestBody.get("content");
        String title = (String) requestBody.get("title");
        List<String> recipientNames = (List<String>) requestBody.get("recipientNames");
        AppUser appUser = appUserRepository.findByAppUserID(userId);
        Optional<Message> optionalMessage = messageService.sendMessage(title, content, appUser, recipientNames);


        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("Location", "/message/" + message.getId())
                    .body(message);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/message/{userId}/{messageId}/replyMessage")
    public ResponseEntity<Message> replyMessage(
            @PathVariable Long messageId,
            @PathVariable Long userId,
            @RequestBody Map<String, Object> requestBody
    ) {
        String content = (String) requestBody.get("content");

        Optional<Message> optionalMessage = messageService.sendReplyMessage(messageId, userId, content);

        if (optionalMessage.isPresent()) {
            return ResponseEntity.ok(optionalMessage.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
