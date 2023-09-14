package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
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
    public ResponseEntity<?> sendMessage(Long senderId, String content, List<String> recipientNames) {
        AppUser sender = repository.findByAppUserID(senderId);
        if (sender == null) {
            return new ResponseEntity<>("Sender not found.", HttpStatus.NOT_FOUND);
        }

        Message message = new Message(content, sender, true);

        for (String recipientName : recipientNames) {
            message.addRecipient(recipientName);
        }

        for (String recipientName : recipientNames) {
            AppUser recipient = repository.findByUsername(recipientName);
            if (recipient == null) {
                return new ResponseEntity<>("Recipient " + recipientName + " not found.", HttpStatus.NOT_FOUND);
            }
            recipient.getMessages().add(message);
            repository.save(recipient);
        }

        message.setIncoming(false);
        sender.getMessages().add(message);
        repository.save(sender);

        return ResponseEntity.ok(message);
    }



    @Transactional
    public ResponseEntity<Message> sendReplyMessage(Long senderId, String content, Long parentSenderId, Long messageId) {
        AppUser sender = repository.findByAppUserID(senderId);
        AppUser parentSender = repository.findByAppUserID(parentSenderId);

        if (sender == null || parentSender == null) {
            return ResponseEntity.badRequest().body(null);
        }

        String senderusername = repository.findByAppUserID(senderId).getUsername();

        Message newMessage = new Message(content, sender, true);
        List<Message> parentSenderMessages = parentSender.getMessages();
        //the message is not saved to the sender, but the recipient.
        for (Message mes : parentSenderMessages) {
            if (mes.getId().equals(messageId)) {
                mes.getChildMessages().add(newMessage);
                newMessage.setParentMessage(mes);
                //We exclude the replier itself from the recipients list.
                //it shouldn't cause any problems because recipients are chosen from the initial message
                //not the latest reply
                List<String> filteredRecipients = mes.getRecipients().stream()
                        .filter(r -> !r.equals(senderusername))
                        .collect(Collectors.toList());
                newMessage.setRecipients(filteredRecipients);
                break;
            }
        }

        parentSender.setMessages(parentSenderMessages);
        repository.save(parentSender);

        return ResponseEntity.ok(newMessage);
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

