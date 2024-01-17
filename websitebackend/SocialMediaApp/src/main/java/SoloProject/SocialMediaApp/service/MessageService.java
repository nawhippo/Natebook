package SoloProject.SocialMediaApp.service;
import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.MessageThread;
import SoloProject.SocialMediaApp.models.Notification;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.MessageRepository;
import SoloProject.SocialMediaApp.repository.MessageThreadRepository;
import SoloProject.SocialMediaApp.repository.NotificationRepository;
import com.mysql.jdbc.Messages;
import org.aspectj.weaver.ast.Not;
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

    private final NotificationRepository notificationRepository;

    public MessageService(MessageRepository messageRepository, MessageThreadRepository messageThreadRepository, AppUserRepository appUserRepository, NotificationRepository notificationRepository) {
        this.messageRepository = messageRepository;
        this.messageThreadRepository = messageThreadRepository;
        this.appUserRepository = appUserRepository;
        this.notificationRepository = notificationRepository;
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

        for (String username : recipientusernames) {
            AppUser appUser = appUserRepository.findByUsername(username);
            Notification notification = new Notification(appUser.getId(), "Message", message.getId(), message.getThreadid());
            System.out.println("Notification stored for user " + notification.getUserId() + " with message ID: " + notification.getObjectId());
            notificationRepository.save(notification);
        }
        return ResponseEntity.ok(message);
    }

    @Transactional
    public List<MessageThread> getAllThreadsByUserId(Long userId) {
        return messageThreadRepository.findByParticipantsContains(userId);
    }


    @Transactional
    public List<Message> getAllMessagesByThreadId(Long threadId, Long userId) {
        List<Message> messages = messageRepository.findByThreadid(threadId);
        System.out.println(messages);
        for (Message message : messages) {
            notificationRepository.deleteByObjectIdAndUserIdAndType(message.getId(), userId, "Message");
        }
        return messages;
    }

    @Transactional
    public Message replytoExistingThread(Long threadId, String content, Long userId) {

        Message message = new Message(userId, threadId, content);
        message = messageRepository.save(message);


        MessageThread thread = messageThreadRepository.getReferenceById(threadId);


        List<Notification> notifications = new ArrayList<>();
        for (Long recipientId : thread.getParticipants()) {
            if (!recipientId.equals(userId)) {
                notifications.add(new Notification(recipientId, "Message", message.getId(), threadId));
            }
        }

        notificationRepository.saveAll(notifications);

        return message;
    }



    public ResponseEntity<?> getNewMessagesForUserLength(Long userId) {
        return ResponseEntity.ok(notificationRepository.countByUserIdAndNotificationType(userId, "Message"));
    }




    public int getMessageNotificationCountByThreadId(Long threadId, Long userId) {
        return notificationRepository.countByUserIdAndThreadIdAndNotificationType(userId, threadId, "Message");
    }
}