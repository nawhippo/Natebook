package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import SoloProject.SocialMediaApp.repository.AppUserRepository;

import java.util.List;


//intermediary between model and database (SoloProject.SocialMediaApp.repository)
public class AppUserServiceImpl implements AppUserService {

    AppUserRepository repository;


    @Override
    public AppUser createUser(String firstName, String lastName, String username, String email){
    AppUser newuser = new AppUser(firstName, lastName, username, email);
    saveUser(newuser);
    return newuser;
    }

    @Override
    public ResponseEntity<AppUser> saveUser(AppUser appUser) {
        repository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
    }

    @Override
    public ResponseEntity<AppUser> findByUserID(Long id) {
        AppUser appUser = repository.findByUserID(id);
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
        AppUser appUser = findByUserID(userId).getBody();
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
        AppUser appUser = findByUserID(userId).getBody();
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
        AppUser appUser = findByUserID(userId).getBody();
        if (appUser != null) {
            return (ResponseEntity<List<Post>>) appUser.getPosts();
        } else {

            return ResponseEntity.notFound().build();
        }
    }



    @Override
    public ResponseEntity<List<Message>> getAllMessages(Long userId) {
        AppUser appUser = findByUserID(userId).getBody();
        if (appUser != null) {
            return (ResponseEntity<List<Message>>) appUser.getMessages();
        } else {

            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<List<AppUser>> getFriends(Long userId) {
        AppUser appUser = findByUserID(userId).getBody();
        return (ResponseEntity<List<AppUser>>) appUser.getFriends();
    }


}