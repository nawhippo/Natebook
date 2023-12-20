package SoloProject.SocialMediaApp.service;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.MessageThread;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.MessageRepository;
import SoloProject.SocialMediaApp.repository.MessageThreadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;



@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageThreadRepository messageThreadRepository;
    private final AppUserRepository repository;

    public MessageService(MessageRepository messageRepository, MessageThreadRepository messageThreadRepository, AppUserRepository repository) {
        this.messageRepository = messageRepository;
        this.messageThreadRepository = messageThreadRepository;
        this.repository = repository;
    }

    @Transactional
    public Optional<Message> sendMessage(String content, Long senderId, List<Long> recipientIds) {
        Optional<MessageThread> thread = messageThreadRepository.findMessageThreadByExactParticipants(recipientIds, recipientIds.size());
        MessageThread currentThread;

        if (thread.isPresent()) {
            currentThread = thread.get();
        } else {
            currentThread = new MessageThread();
            currentThread.setParticipants(recipientIds);
            currentThread = messageThreadRepository.save(currentThread);
        }

        Message message = new Message(senderId, currentThread.getId(), content);
        message = messageRepository.save(message);

        return Optional.of(message);
    }
}





