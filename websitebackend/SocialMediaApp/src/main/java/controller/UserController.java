package controller;
import models.Message;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import repository.*;

@Controller
@RequestMapping("/users")
public class UserController {
    private  UserRepository userRepository;

    @GetMapping
    public String handleRequest(){
        return "Welcome to the users page!";
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserbyId(@PathVariable Long userId){
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(user);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/{messageId}")
    public ResponseEntity<Message> getMessagebyId(@PathVariable Long userId, @PathVariable Long messageId){
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            for (int i = 0; i < user.getMessages().size(); i++) {
                if (user.getMessages().get(i).getId() == messageId) {
                    return ResponseEntity.ok(user.getMessages().get(i));
                }
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }
}
