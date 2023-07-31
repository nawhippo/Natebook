package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


//intermediary between model and database (SoloProject.SocialMediaApp.repository)
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository repository;

    public AppUserServiceImpl(AppUserRepository repository) {
        this.repository = repository;
    }


    @Override
    public AppUser createUser(String firstName, String lastName, String username, String email, String password){
    AppUser newuser = new AppUser(firstName, lastName, username, email, password);
    saveUser(newuser);
    return newuser;
    }



    @Override
    public ResponseEntity<AppUser> saveUser(AppUser appUser) {
        repository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
    }

    @Override
    public ResponseEntity<AppUser> findByAppUserID(Long id) {
        AppUser appUser = repository.findByAppUserID(id);
        if (appUser != null) {
            return ResponseEntity.ok(appUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Override
    public ResponseEntity<AppUser> findByUsername(String username) {
        AppUser appUser = repository.findByUsername(username);
        if (appUser != null) {
            return ResponseEntity.ok(appUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<AppUser>> findByFirstname(String firstname) {
        List<AppUser> appUsers = repository.findByFirstname(firstname);
        return ResponseEntity.ok(appUsers);

    }


    @Override
    public ResponseEntity<List<AppUser>> findByLastname(String lastname) {
        List<AppUser> appUsers = repository.findByLastname(lastname);
        return ResponseEntity.ok(appUsers);
    }

    @Override
    public ResponseEntity<List<AppUser>> findByFirstNameAndLastName(String firstname, String lastname) {
        List<AppUser> appUsers = repository.findByFirstnameAndLastname(firstname, lastname);
        return ResponseEntity.ok(appUsers);
    }


    //Post functions
    @Override
    public ResponseEntity<Post> getPostById(Long userId, Long postId){
        AppUser appUser = findByAppUserID(userId).getBody();
        if (appUser != null) {
            for (Post post : appUser.getPosts()) {
                if (post.getId().equals(postId)) {
                    return ResponseEntity.ok(post);
                }
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Message> getMessageById(Long userId, Long messageId){
        AppUser appUser = findByAppUserID(userId).getBody();
        if (appUser != null) {
            for (Message message : appUser.getMessages()) {
                if (message.getId().equals(messageId)) {
                    return ResponseEntity.ok(message);
                }
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }




    @Override
    public ResponseEntity<List<Post>> getAllPosts(Long userId) {
        AppUser appUser = findByAppUserID(userId).getBody();
        if (appUser != null) {
            return (ResponseEntity<List<Post>>) appUser.getPosts();
        } else {

            return ResponseEntity.notFound().build();
        }
    }



    @Override
    public ResponseEntity<List<Message>> getAllMessages(Long userId) {
        AppUser appUser = findByAppUserID(userId).getBody();
        if (appUser != null) {
            return (ResponseEntity<List<Message>>) appUser.getMessages();
        } else {

            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<List<AppUser>> getFriends(Long userId) {
        AppUser appUser = findByAppUserID(userId).getBody();
        return (ResponseEntity<List<AppUser>>) appUser.getFriends();
    }


    @Override
    public ResponseEntity<AppUser> getFriendById(Long userId, Long friendId) {
        AppUser appUser = findByAppUserID(userId).getBody();
        if (appUser == null) {
            return ResponseEntity.notFound().build();
        } else {
            for (AppUser friend : appUser.getFriends()) {
                if (friend.getAppUserID().equals(friendId)) {
                    return ResponseEntity.ok(friend);
                }
            }
            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<Message> sendMessage(Long senderId, String content, List<Long> recipientIds) {
        // Find the sender user
        AppUser senderUser = findByAppUserID(senderId).getBody();
        if (senderUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Find the recipient users
        List<AppUser> recipients = new ArrayList<>();
        for (Long recipientId : recipientIds) {
            AppUser recipient = findByAppUserID(recipientId).getBody();
            if (recipient != null) {
                recipients.add(recipient);
            }
        }

        // Create the message
        Message message = new Message();
        message.setContent(content);
        message.setSender(senderUser);
        message.setRecipients(recipients);

        // Save the message to the sender's messages list
        senderUser.getMessages().add(message);
        saveUser(senderUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

}