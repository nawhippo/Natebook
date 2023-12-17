//package SoloProject.SocialMediaApp.controller;
//
//import SoloProject.SocialMediaApp.models.CustomMessage;
//import SoloProject.SocialMediaApp.service.JmsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@RestController
//@RequestMapping("/messages")
//public class CustomMessageController {
//
//    private final JmsService jmsService;
//
////    @Autowired
//    public CustomMessageController(JmsService jmsService) {
//        this.jmsService = jmsService;
//    }
//
//
//    //will get all group chats where the user sent a message.
//
//    @PostMapping("/send")
//    public ResponseEntity<?> sendMessageToGroup(@RequestBody Map<String, Object> messageData) {
//        CustomMessage message = new CustomMessage();
//        message.setTitle((String) messageData.get("title"));
//        message.setBody((String) messageData.get("body"));
//        message.setSenderId((Long) messageData.get("senderId"));
//        message.setGroupChatId((Long) messageData.get("groupChatId"));
//        message.setSenderFirstname((String)messageData.get("senderFirstName"));
//        message.setSenderLastname((String)messageData.get("senderLastName"));
//        message.setParticipants((ArrayList<Long>)messageData.get("participants"));
//        jmsService.sendMessageToGroup(message);
//        return ResponseEntity.ok("Message sent to group chat");
//    }
//
//
//    //all active group chats
//    @GetMapping("/{userId}/messages")
//    public ResponseEntity<Map<Long, List<CustomMessage>>> getMessagesByParticipantGroupedByGroupChatId(@PathVariable Long userId) {
//        Map<Long, List<CustomMessage>> groupedMessages = jmsService.getMessagesByParticipantGroupedByGroupChatId(userId);
//        return ResponseEntity.ok(groupedMessages);
//    }
//}