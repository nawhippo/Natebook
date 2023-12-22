package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.MessageThread;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.MessageRepository;
import SoloProject.SocialMediaApp.repository.MessageThreadRepository;
import SoloProject.SocialMediaApp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class MessageController {
    private final MessageService messageService;
    private final AppUserRepository appUserRepository;
    private final MessageThreadRepository messageThreadRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageService messageService, AppUserRepository appUserRepository, MessageThreadRepository messageThreadRepository, MessageRepository messageRepository) {
        this.messageService = messageService;
        this.appUserRepository = appUserRepository;
        this.messageThreadRepository = messageThreadRepository;
        this.messageRepository = messageRepository;
    }


    @PostMapping("/message/{userId}/sendMessage")
    public ResponseEntity<?> sendMessage(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> requestBody
    ) {
        String content = (String) requestBody.get("content");
        List<String> recipientUsernames = (List<String>) requestBody.get("recipientUsernames");

        Optional<Message> sentMessage = messageService.sendMessage(content, userId, recipientUsernames);

        if (sentMessage.isPresent()) {
            return ResponseEntity.ok(sentMessage.get());
        } else {
            return ResponseEntity.badRequest().body("Message could not be sent.");
        }
    }

    @GetMapping("/message/{userId}/getAllThreads")
    public ResponseEntity<List<MessageThread>> getAllThreads(@PathVariable Long userId){
        List<MessageThread> threads = messageService.getAllThreadsByUserId(userId);
        return ResponseEntity.ok(threads);
    }

    @GetMapping("/message/{threadId}/getAllMessages")
    public ResponseEntity<List<Message>> getAllMessageByThread(@PathVariable Long threadId) {
        List<Message> messages = messageService.getAllMessagesByThreadId(threadId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


    @PutMapping("/message/{threadId}/reply")
    public ResponseEntity<Message> replytoThread(@PathVariable Long threadId, @RequestBody Map<String, Object> requestBody) {
        String content = (String) requestBody.get("content");
        Long userId = ((Number) requestBody.get("userId")).longValue();

        Message message = messageService.replytoExistingThread(threadId, content, userId);
        return ResponseEntity.ok(message);
    }
}
