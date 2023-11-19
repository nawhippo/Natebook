package SoloProject.SocialMediaApp.controller;


import SoloProject.SocialMediaApp.models.CustomMessage;
import SoloProject.SocialMediaApp.service.JmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/messages")
public class CustomMessageController {
    @Autowired
    private JmsService jmsService;


    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody CustomMessage message){
        jmsService.sendMessageToGroup(message);
        return ResponseEntity.ok("Message sent successfully");
    }


    @GetMapping("/groupchats")
    public ResponseEntity<List<CustomMessage>> getAllMessagesFromGroups(@RequestParam Long senderid) {
        Set<Long> messageIds = jmsService.getGroupChatsFromSenderId(senderid);
        List<CustomMessage> messageList = jmsService.getAllMessagesFromGroupChats(messageIds);
        return ResponseEntity.ok(messageList);
    }
}
