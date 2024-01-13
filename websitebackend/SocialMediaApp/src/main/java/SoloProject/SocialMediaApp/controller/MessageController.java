package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.MessageThread;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.MessageRepository;
import SoloProject.SocialMediaApp.repository.MessageThreadRepository;
import SoloProject.SocialMediaApp.service.AccountService;
import SoloProject.SocialMediaApp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final AccountService accountService;

    @Autowired
    public MessageController(MessageService messageService, AppUserRepository appUserRepository,
                             MessageThreadRepository messageThreadRepository, MessageRepository messageRepository,
                             AccountService accountService) {
        this.messageService = messageService;
        this.appUserRepository = appUserRepository;
        this.messageThreadRepository = messageThreadRepository;
        this.messageRepository = messageRepository;
        this.accountService = accountService;
    }

    @PostMapping("/message/{userId}/sendMessage")
    public ResponseEntity<?> sendMessage(@PathVariable Long userId,
                                         @RequestBody Map<String, Object> requestBody,
                                         Authentication authentication) {
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        String content = (String) requestBody.get("content");
        List<String> recipientUsernames = (List<String>) requestBody.get("recipientUsernames");

        return messageService.sendMessage(content, userId, recipientUsernames);
    }

    @GetMapping("/message/{userId}/getAllThreads")
    public ResponseEntity<?> getAllThreads(@PathVariable Long userId,
                                                             Authentication authentication) {
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        List<MessageThread> threads = messageService.getAllThreadsByUserId(userId);
        return ResponseEntity.ok(threads);
    }

    @GetMapping("/message/{threadId}/getAllMessages")
    public ResponseEntity<?> getAllMessageByThread(@PathVariable Long threadId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Long appUserID = appUserRepository.findByUsername(username).getAppUserID();

        Optional<MessageThread> thread = messageThreadRepository.findById(threadId);

        if (thread.isPresent() && thread.get().getParticipants().contains(appUserID)) {
            List<Message> messages = messageService.getAllMessagesByThreadId(threadId);


            messageService.updateUserLastChecked(appUserID);

            return new ResponseEntity<>(messages, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied or Thread Not Found");
        }
    }

    @GetMapping("/message/{userId}/getMessageNotifications")
    public ResponseEntity<?> getUserMessageCount(@PathVariable Long userId,
                                                   Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String authenticatedUsername = userDetails.getUsername();
        Long authenticatedUserId = appUserRepository.findByUsername(authenticatedUsername).getAppUserID();
        if (!authenticatedUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return messageService.getNewMessagesForUserLength(userId);
    }

    @PutMapping("/message/{threadId}/reply")
    public ResponseEntity<?> replytoThread(@PathVariable Long threadId,
                                                 @RequestBody Map<String, Object> requestBody,
                                                 Authentication authentication) {
        Long userId = ((Number) requestBody.get("userId")).longValue();
        String authenticatedUsername = appUserRepository.findByAppUserID(userId).getUsername();
        if (!accountService.checkAuthenticationMatch(authenticatedUsername, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
        String content = (String) requestBody.get("content");

        Message message = messageService.replytoExistingThread(threadId, content, userId);
        return ResponseEntity.ok(message);
    }
}