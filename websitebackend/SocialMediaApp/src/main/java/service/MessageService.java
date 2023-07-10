package service;

import models.Message;
import models.User;

import java.util.List;

public interface MessageService {
    void sendMessage(List<User> recipients, Message message);
    public Message findMessage(User user, Long messageId);
}


