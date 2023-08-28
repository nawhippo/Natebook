package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.UserDTO;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service

public class FriendRequestService {

    private final AppUserRepository repository;



    public FriendRequestService(AppUserRepository repository, PasswordEncoder passwordEncoder, EntityManager entityManager) {
        this.repository = repository;
    }



    public ResponseEntity<List<UserDTO>> getAllFriendRequestsDTOS(Long UserId) {
        AppUser user = repository.findByAppUserID(UserId);
        List<Long> friendreqs = user.getRequests();
        List<UserDTO> DTOList = convertToDTOList(friendreqs);
        for (UserDTO dto : DTOList) {
            System.out.println(dto);
        }
        return ResponseEntity.ok(DTOList);
    }
    private List<UserDTO> convertToDTOList(List<Long> ids) {
        List<UserDTO> result = new ArrayList<>();
        if (ids != null) {
            for (long id : ids) {
                result.add(convertToDTO(id));
            }
        }
        return result;
    }

    private UserDTO convertToDTO(Long id) {
        AppUser user = repository.findByAppUserID(id);
        if (user != null) {
            return new UserDTO(
                    id, user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail()
            );
        }
        return null;
    }





    //strings are usernames

    public ResponseEntity<AppUser> sendFriendRequest(Long senderId, Long friendId) {
        AppUser recipient = repository.findByAppUserID(friendId);
        if(senderId == friendId){
            return new ResponseEntity<>(recipient, HttpStatus.BAD_REQUEST);
        }
        List<Long> newRequests = recipient.getRequests();
        if(newRequests.contains(senderId)){
            return new ResponseEntity<>(recipient, HttpStatus.BAD_REQUEST);
        }
        newRequests.add(senderId);
        recipient.setRequests(newRequests);
        repository.save(recipient);
        return new ResponseEntity<>(recipient, HttpStatus.OK);
    }

    public ResponseEntity<AppUser> sendFriendRequest(Long senderId, String recipientUsername) {
        AppUser recipient = repository.findByUsername(recipientUsername);
        if(senderId == recipient.getAppUserID()){
            return new ResponseEntity<>(recipient, HttpStatus.BAD_REQUEST);
        }
        if (recipient.getFriends().contains(senderId)) {
            return new ResponseEntity<>(recipient, HttpStatus.BAD_REQUEST);
        }
        if (recipient.getRequests() == null) {
            recipient.setRequests(new ArrayList<>());
        }

        List<Long> newRequests = recipient.getRequests();
        newRequests.add(senderId);
        recipient.setRequests(newRequests);

        repository.save(recipient);

        return new ResponseEntity<>(recipient, HttpStatus.OK);
    }


    public ResponseEntity<AppUser> acceptFriendRequest(Long recipientId, Long senderId) {
        AppUser recipient = repository.findByAppUserID(recipientId);
        if(recipient.getFriends().contains(senderId)){
            return new ResponseEntity<>(recipient, HttpStatus.BAD_REQUEST);
        }
        AppUser sender = repository.findByAppUserID(senderId);
        List<Long> newRequests = recipient.getRequests();
        newRequests.remove(senderId);
        List<Long> recipientFriends = recipient.getFriends();
        List<Long> senderFriends = sender.getFriends();
        recipientFriends.add(senderId);
        senderFriends.add(recipientId);
        recipient.setFriends(recipientFriends);
        sender.setFriends(senderFriends);
        repository.save(sender);
        repository.save(recipient);
        return new ResponseEntity<>(sender, HttpStatus.OK);
    }



    public ResponseEntity<AppUser> declineFriendRequest(Long recipientId, Long senderId) {
        AppUser appUser = repository.findByAppUserID(recipientId);
        List<Long> requests = appUser.getRequests();
        requests.remove(senderId);
        appUser.setRequests(requests);
        repository.save(appUser);
        return new ResponseEntity<>(appUser, HttpStatus.OK);
    }


    public ResponseEntity<AppUser> saveUser(AppUser appUser) {
        repository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
    }




}
