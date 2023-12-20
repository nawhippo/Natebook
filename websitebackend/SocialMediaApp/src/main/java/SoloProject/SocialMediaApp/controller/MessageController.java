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


    @PostMapping("/message/{userId}/sendMessage")
    public ResponseEntity<?> sendMessage(@PathVariable Long userId,
                                         @RequestParam String content,
                                         @RequestParam List<Long> recipientIds) {
        Optional<Message> sentMessage = messageService.sendMessage(content, userId, recipientIds);

        if (sentMessage.isPresent()) {
            return ResponseEntity.ok(sentMessage.get());
        } else {
            return ResponseEntity.badRequest().body("Message could not be sent.");
        }
    }

}
