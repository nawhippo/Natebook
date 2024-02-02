package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.AppUserDTO;
import SoloProject.SocialMediaApp.models.Notification;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.NotificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service

public class FriendRequestService {

    private final AppUserRepository appUserRepository;
    private final NotificationRepository notificationRepository;


    public FriendRequestService(AppUserRepository appUserRepository, NotificationRepository notificationRepository) {
        this.appUserRepository = appUserRepository;
        this.notificationRepository = notificationRepository;
    }



    public ResponseEntity<List<AppUserDTO>> getAllFriendRequestsDTOS(Long userId) {
        AppUser user = appUserRepository.findByAppUserID(userId);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Long> friendRequests = user.getRequests();
        List<AppUserDTO> dtoList = convertToDTOList(friendRequests);

        for (Long senderId : friendRequests) {
            Notification notification = new Notification(userId, "FriendRequest", senderId);
            notificationRepository.delete(notification);
        }

        return ResponseEntity.ok(dtoList);
    }

    private List<AppUserDTO> convertToDTOList(List<Long> ids) {
        List<AppUserDTO> result = new ArrayList<>();
        if (ids != null) {
            for (long id : ids) {
                result.add(convertToDTO(id));
            }
        }
        return result;
    }
    private AppUserDTO convertToDTO(Long id) {
        AppUser user = appUserRepository.findByAppUserID(id);
        if (user != null) {
            return new AppUserDTO(
                    user
            );
        }
        return null;
    }


    //strings are usernames

    public ResponseEntity<AppUser> sendFriendRequest(Long senderId, Long friendId) {
        AppUser recipient = appUserRepository.findByAppUserID(friendId);
        if(senderId == friendId){
            return new ResponseEntity<>(recipient, HttpStatus.BAD_REQUEST);
        }
        List<Long> newRequests = recipient.getRequests();
        if(newRequests.contains(senderId)) {
            return new ResponseEntity<>(recipient, HttpStatus.BAD_REQUEST);
        }
        if(recipient.getBlockList().contains(senderId)){
            AppUser errorUser = new AppUser();
            errorUser.setUsername("Recipient " + recipient.getUsername() + " blocked Sender.");
            return new ResponseEntity<>(errorUser, HttpStatus.BAD_REQUEST);
        }
        newRequests.add(senderId);
        recipient.setRequests(newRequests);
        appUserRepository.save(recipient);
        return new ResponseEntity<>(recipient, HttpStatus.OK);
    }

    public ResponseEntity<AppUser> sendFriendRequest(Long senderId, String recipientUsername) {
        AppUser recipient = appUserRepository.findByUsername(recipientUsername);
        if(senderId == recipient.getAppUserID()){
            return new ResponseEntity<>(recipient, HttpStatus.BAD_REQUEST);
        }
        if(recipient.getBlockList().contains(senderId)){
            AppUser errorUser = new AppUser();
            errorUser.setUsername("Recipient " + recipientUsername + " blocked Sender.");
            return new ResponseEntity<>(errorUser, HttpStatus.BAD_REQUEST);
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
        Notification notification = new Notification(recipient.getAppUserID(), "FriendRequest", recipient.getAppUserID());
        appUserRepository.save(recipient);

        return new ResponseEntity<>(recipient, HttpStatus.OK);
    }


    public ResponseEntity<AppUser> acceptFriendRequest(Long recipientId, Long senderId) {
        AppUser recipient = appUserRepository.findByAppUserID(recipientId);
        AppUser sender = appUserRepository.findByAppUserID(senderId);

        // Check if they are already friends
        if(recipient.getFriends().contains(senderId)){
            return new ResponseEntity<>(recipient, HttpStatus.BAD_REQUEST);
        }

        recipient.getRequests().remove(senderId);
        recipient.getFriends().add(senderId);
        recipient.setFriendCount(recipient.getFriendCount() + 1);

        sender.getFriends().add(recipientId);
        sender.setFriendCount(sender.getFriendCount() + 1);

        appUserRepository.save(sender);
        appUserRepository.save(recipient);

        return new ResponseEntity<>(sender, HttpStatus.OK);
    }



    public ResponseEntity<AppUser> declineFriendRequest(Long recipientId, Long senderId) {
        AppUser appUser = appUserRepository.findByAppUserID(recipientId);
        List<Long> requests = appUser.getRequests();
        requests.remove(senderId);
        appUser.setRequests(requests);
        appUserRepository.save(appUser);
        return new ResponseEntity<>(appUser, HttpStatus.OK);
    }



    public ResponseEntity<List<Notification>> getAllFriendRequestNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndNotificationType(userId, "FriendRequest");

        if (notifications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(notifications);
    }
}
