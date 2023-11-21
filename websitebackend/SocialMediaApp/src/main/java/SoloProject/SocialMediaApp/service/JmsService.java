package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.CustomMessage;
import SoloProject.SocialMediaApp.repository.CustomMessageRepository;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JmsService {

    private static final Logger logger = LoggerFactory.getLogger(JmsService.class);

    private final CustomMessageRepository customMessageRepository;
    private final JmsTemplate jmsTemplate;

    public JmsService(CustomMessageRepository customMessageRepository, JmsTemplate jmsTemplate) {
        this.customMessageRepository = customMessageRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "queue.sample")
    public void receiveMessage(CustomMessage message) {
        logger.info("Received message: {}", message);
        try {
            customMessageRepository.save(message);
        } catch (Exception e) {
            logger.error("Error saving message: {}", e.getMessage(), e);
        }
    }

    public void sendMessageToGroup(CustomMessage message) {
        String groupChatTopic = resolveGroupChatTopic(message.getGroupChatId());
        jmsTemplate.convertAndSend(groupChatTopic, message);
    }

    private String resolveGroupChatTopic(long chatGroupId) {
        return "group.chat." + chatGroupId;
    }


    public Map<Long, List<CustomMessage>> getMessagesByParticipantGroupedByGroupChatId(Long userId) {
        List<CustomMessage> allMessages = customMessageRepository.findAll();

        return allMessages.stream()
                .filter(msg -> msg.getParticipants().contains(userId))
                .collect(Collectors.groupingBy(CustomMessage::getGroupChatId));
    }
}