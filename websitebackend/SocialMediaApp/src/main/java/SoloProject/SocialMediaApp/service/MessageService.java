package SoloProject.SocialMediaApp.service;
import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.MessageThread;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.MessageRepository;
import SoloProject.SocialMediaApp.repository.MessageThreadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Optional<Message> sendMessage(String content, Long senderId, List<String> recipientusernames) {
       List<Long> recipientIds = new ArrayList<>();
        for(String username : recipientusernames) {
            AppUser appUser = appUserRepository.findByUsername(username);
            recipientIds.add(appUser.getId());
        }

            MessageThread currentThread = new MessageThread();
            currentThread.setParticipants(recipientIds);
            currentThread.getParticipants().add(senderId);
            currentThread.setParticipantsNames(recipientusernames);
            currentThread = messageThreadRepository.save(currentThread);

        Message message = new Message(senderId, currentThread.getId(), content);
        message = messageRepository.save(message);

        return Optional.of(message);
    }


    @Transactional
    public List<MessageThread> getAllThreadsByUserId(Long userId) {
        return messageThreadRepository.findByParticipantsContains(userId);
    }

    public List<Message> getAllMessagesByThreadId(Long threadId) {
        return messageRepository.findByThreadid(threadId);
    }

    @Transactional
    public Message replytoExistingThread(Long threadId, String content, Long userId){
        Message message = new Message(userId, threadId, content);
        message = messageRepository.save(message);
        return message;
    }
}





