package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final AppUserRepository repository;

    public MessageService(AppUserRepository repository, MessageRepository messageRepository) {
        this.repository = repository;
        this.messageRepository = messageRepository;
    }


    public List<Message> getAllRecMessages(Long userId) {
        AppUser appUser = repository.findByAppUserID(userId);
        if (appUser == null) {
            return null;
        }
        return messageRepository.findByRecipientsContains(appUser.getUsername());
    }

    public List<Message> getAllSentMessages(Long userId) {
        AppUser appUser = repository.findByAppUserID(userId);
        if (appUser == null) {
            return null;
        }
        return messageRepository.findBySender(appUser);
    }






    public Optional<Message> sendMessage(String title, String content, AppUser sender, List<String> recipients) {
        try {
            Message newMessage = new Message();
            newMessage.setContent(content);
            newMessage.setSender(sender);
            newMessage.setRecipients(recipients);
            newMessage.setDateTime(new Date());
            newMessage.setIncoming(true);
            newMessage.setSenderusername(sender.getUsername());
            newMessage.setTitle(title);
            Message savedMessage = messageRepository.save(newMessage);
            return Optional.of(savedMessage);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Message> sendReplyMessage(Long messageId, Long userid, String content) {
        Optional<Message> optionalParentMessage = messageRepository.findById(messageId);
        AppUser appUser = repository.findByAppUserID(userid);

        if (optionalParentMessage.isPresent() && appUser != null) {
            Message parentMessage = optionalParentMessage.get();

            Message newMessage = new Message(content, appUser, parentMessage.getRecipients());
            newMessage.setParentMessage(parentMessage);

            parentMessage.getChildMessages().add(newMessage);

            Message savedMessage = messageRepository.save(newMessage);
            return Optional.of(savedMessage);
        }

        return Optional.empty();
    }


    }






