package SoloProject.SocialMediaApp.service;
import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.MessageThread;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.MessageRepository;
import SoloProject.SocialMediaApp.repository.MessageThreadRepository;
import com.mysql.jdbc.Messages;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageThreadRepository messageThreadRepository;
    private final AppUserRepository appUserRepository;

    public MessageService(MessageRepository messageRepository, MessageThreadRepository messageThreadRepository, AppUserRepository appUserRepository) {
        this.messageRepository = messageRepository;
        this.messageThreadRepository = messageThreadRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public ResponseEntity<?> sendMessage(String content, Long senderId, List<String> recipientusernames) {
        List<Long> recipientIds = new ArrayList<>();
        for (String username : recipientusernames) {
            AppUser appUser = appUserRepository.findByUsername(username);
            if (appUser == null || !appUser.getFriends().contains(senderId) && !appUser.isPrivate() || appUser.getBlockList().contains(senderId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Message cannot be sent due to privacy settings or block list.");
            }
            recipientIds.add(appUser.getId());
        }

        MessageThread currentThread = new MessageThread();
        currentThread.setParticipants(recipientIds);
        currentThread.getParticipants().add(senderId);
        currentThread.setParticipantsNames(recipientusernames);
        currentThread = messageThreadRepository.save(currentThread);

        Message message = new Message(senderId, currentThread.getId(), content);
        message = messageRepository.save(message);

        return ResponseEntity.ok(message);
    }

    @Transactional
    public List<MessageThread> getAllThreadsByUserId(Long userId) {
        return messageThreadRepository.findByParticipantsContains(userId);
    }

    @Transactional
    public ResponseEntity<?> getAllUserMessageCount(Long userId) {
        int totalMessages = 0;
        for (MessageThread thread : messageThreadRepository.findByParticipantsContains(userId)) {
            totalMessages += messageThreadRepository.countMessagesInThreadByOtherUsers(thread.getId(), userId);
        }
        return ResponseEntity.ok(totalMessages);
    }


    public List<Message> getAllMessagesByThreadId(Long threadId) {
        return messageRepository.findByThreadid(threadId);
    }

    @Transactional
    public Message replytoExistingThread(Long threadId, String content, Long userId) {
        Message message = new Message(userId, threadId, content);
        message = messageRepository.save(message);
        return message;
    }

    @Transactional
    public void updateUserLastChecked(Long userId) {
        Optional<AppUser> userOptional = appUserRepository.findById(userId);
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            user.setLastChecked(LocalDateTime.now());
            appUserRepository.save(user);
        }
    }

    @Transactional
    public ResponseEntity<?> getNewMessagesForUserLength(Long userId) {
        int newMessagesCount = 0;
        Optional<AppUser> userOptional = appUserRepository.findById(userId);

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        AppUser user = userOptional.get();
        LocalDateTime lastChecked = user.getLastChecked();

        if (lastChecked == null) {
            lastChecked = LocalDateTime.of(2000, 1, 1, 0, 0);
        }


        for (MessageThread thread : messageThreadRepository.findByParticipantsContains(userId)) {
            List<Message> threadMessages = messageRepository.findByThreadid(thread.getId());
            for (Message message : threadMessages) {
                if (message.getTimestamp().isAfter(lastChecked)) {
                    newMessagesCount++;
                    System.out.println("New message found!");
                }
            }
        }

        return ResponseEntity.ok(newMessagesCount);
    }


}


