package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import SoloProject.SocialMediaApp.service.JmsService;
@RestController
public class JmsController {

    @Autowired
    private JmsTemplate jmsTemplate;

    // Send a message to a specified destination
    @GetMapping("/send/{userId}")
    public String sendMessage(@PathVariable String userId, @RequestBody Message message) {
        String userQueue = userId + ".queue";
        jmsTemplate.convertAndSend("queue.sample", message.getContent());
        return "Message sent to userqueue: " + userQueue;
    }


}