package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.CustomMessage;
import SoloProject.SocialMediaApp.repository.CustomMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JmsService {
    @Autowired
    private final CustomMessageRepository customMessageRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    public JmsService(CustomMessageRepository customMessageRepository) {
        this.customMessageRepository = customMessageRepository;
    }

    // This method can be used to process received messages
    @JmsListener(destination = "queue.sample")
    public void receiveMessage(CustomMessage message) {
        // Logic for processing received messages, e.g., logging, saving to a database
    }

    // Method for sending a message to a group chat topic
    public void sendMessageToGroup(CustomMessage message) {
        String groupChatTopic = resolveGroupChatTopic(message.getChatGroupId());
        jmsTemplate.convertAndSend(groupChatTopic, message);
    }

    // Resolving the group chat topic name based on the chatGroupId
    private String resolveGroupChatTopic(long chatGroupId) {
        // The topic name is created using a convention like "group.chat." + chatGroupId
        return "group.chat." + chatGroupId;
    }

    // Find group chats by the sender's ID
    public Set<Long> getGroupChatsFromSenderId(Long senderId) {
        List<CustomMessage> sentMessages = customMessageRepository.findBySender(senderId);
        Set<Long> groupChatIds = new HashSet<>();

        for (CustomMessage message : sentMessages) {
            groupChatIds.add(message.getChatGroupId());
        }

        return groupChatIds;
    }

    // Retrieve all messages from a set of group chats
    public List<CustomMessage> getAllMessagesFromGroupChats(Set<Long> groupChatIds) {
        List<CustomMessage> allMessages = new ArrayList<>();
        for (Long chatGroupId : groupChatIds) {
            List<CustomMessage> messages = customMessageRepository.findByGroupChatId(chatGroupId);
            allMessages.addAll(messages);
        }
        return allMessages;
    }
}