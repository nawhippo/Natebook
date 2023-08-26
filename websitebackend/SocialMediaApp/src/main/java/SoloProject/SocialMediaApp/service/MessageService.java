package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class MessageService {

    private final AppUserRepository repository;

    public MessageService(AppUserRepository repository) {
        this.repository = repository;
    }
    public ResponseEntity<Message> getMessageById(Long userId, Long messageId) {
        AppUser appUser = repository.findByAppUserID(userId);
        if (appUser != null) {
            for (Message message : appUser.getMessages()) {
                if (message.getId().equals(messageId)) {
                    return ResponseEntity.ok(message);
                }
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }



    public ResponseEntity<List<Message>> getAllMessages(Long userId) {
        AppUser appUser = repository.findByAppUserID(userId);
        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }
        List<Message> messages = appUser.getMessages();
        return ResponseEntity.ok(messages);
    }

    public ResponseEntity<List<Message>> getMessagebyUsername(Long userId) {
        AppUser appUser = repository.findByAppUserID(userId);
        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }
        List<Message> messages = appUser.getMessages();
        return ResponseEntity.ok(messages);
    }

    @Transactional
    public ResponseEntity<Message> sendMessage(Long senderId, String content, List<String> recipientNames) {
        AppUser sender = repository.findByAppUserID(senderId);
        Message message = new Message(content, sender, false);

        for (String recipientName : recipientNames) {
            message.addRecipient(recipientName);
        }

        for (String recipientName : recipientNames) {
            AppUser recipient = repository.findByUsername(recipientName);
            recipient.getMessages().add(message);
            repository.save(recipient);
        }
        message.setIncoming(true);
        sender.getMessages().add(message);
        repository.save(sender);

        return ResponseEntity.ok(message);
    }



    public ResponseEntity<List<Message>> getMessagesByUsername(Long userid, String username){
    AppUser user = repository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Message> messages = user.getMessages();
        return ResponseEntity.ok(messages);
    }
    }

