package service;

import models.Message;
import models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Override
    public void sendMessage(List<User> recipients, Message message) {
        for(int i = 0; i < recipients.size(); i++) {
            recipients.get(i).getMessages().add(message);
        }
    }

    @Override
    public Message findMessage(User user, Long messageId){
        for (Message message : user.getMessages()) {
            if (message.getId().equals(messageId)) {
                return message;
            }
        }
        return null; // Message not found
    }
}
