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
    public AppUser createUser(String firstName, String lastName, String username, String email, String password) {
        if (repository.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username is already taken. Please choose a different username.");
        }


        AppUser newuser = new AppUser(firstName, lastName, username, email, password);
        repository.save(newuser);
        return newuser;
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
    public ResponseEntity<AppUser> saveUser(AppUser appUser) {
        repository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
    }

    @Override
    public ResponseEntity<List<AppUser>> findUser(String firstname, String lastname) {
        return null;
    }


    //should only return 1 user
    @Override
    public ResponseEntity<AppUser> findUser(Long id) {
        AppUser appUser = repository.findByAppUserID(id);
        if (appUser != null) {
            return ResponseEntity.ok(appUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //should only return 1 user
    @Override
    public ResponseEntity<AppUser> findUser(String username) {
        AppUser appUser = repository.findByUsername(username);
        if (appUser != null) {
            return ResponseEntity.ok(appUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @Override
//    public ResponseEntity<AppUser> addFriend(Long userId, String username) {
//        AppUser appUser = repository.findByAppUserID(userId);
//        AppUser friend = repository.findByUsername(username);
//
//        if(friend.equals(null)){
//            return ResponseEntity.notFound().build();
//        } else {
//            List<AppUser> friends = appUser.getFriends();
//            friends.add(friend);
//            appUser.setFriends(friends);
//            AppUser updatedUser = repository.save(appUser);
//            return ResponseEntity.ok(updatedUser);
//        }
//    }

    @Override
    public ResponseEntity<List<AppUser>> findRelatedUsers(String firstname, String lastname) {
        List<AppUser> appUsers;

        if (firstname != null && lastname != null) {
            appUsers = repository.findByFirstnameAndLastname(firstname, lastname);
        } else if (firstname != null) {
            appUsers = repository.findByFirstname(firstname);
        } else if (lastname != null) {
            appUsers = repository.findByLastname(lastname);
        } else {
            // No search criteria provided
            // You may choose to return an empty list or an error response here
            return ResponseEntity.badRequest().build();
        }

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
        ResponseEntity<AppUser> response = findByAppUserID(userId);

        if (response != null && response.getBody() != null) {
            AppUser appUser = response.getBody();
            return ResponseEntity.ok(appUser.getFriends());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @Override
    public ResponseEntity<AppUser> getFriend(Long userId, Long friendId) {
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
    public ResponseEntity<AppUser> getFriend(Long userId, String friendUsername) {
        AppUser appUser = findByAppUserID(userId).getBody();
        if (appUser == null) {
            return ResponseEntity.notFound().build();
        } else {
            for (AppUser friend : appUser.getFriends()) {
                if (friend.getUsername().equals(friendUsername)) {
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


    @Override
    public ResponseEntity<List<AppUser>> getAllFriendRequests(Long UserId){
        AppUser user = repository.findByAppUserID(UserId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.getRequests());
    }

    //strings are usernames
    @Override
    public ResponseEntity<AppUser> sendFriendRequest(Long senderId, Long friendId) {
        AppUser friend = repository.findByAppUserID(friendId);
        List<AppUser> newRequests = friend.getRequests();
        AppUser sender = repository.findByAppUserID(senderId);
        newRequests.add(sender);
        friend.setRequests(newRequests);
        repository.save(friend);
        return new ResponseEntity<>(friend, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AppUser> sendFriendRequest(Long senderId, String recipientUsername) {
        AppUser recipient = repository.findByUsername(recipientUsername);
        List<AppUser> newRequests = recipient.getRequests();
        AppUser sender = repository.findByAppUserID(senderId);
        newRequests.add(sender);
        recipient.setRequests(newRequests);
        repository.save(recipient);
        return new ResponseEntity<>(recipient, HttpStatus.OK);
    }

    //have to account for the fact that friends are Strings representing usernames.
    @Override
    public ResponseEntity<AppUser> acceptFriendRequest(Long recipientId, String senderUsername) {
        AppUser recipient = repository.findByAppUserID(recipientId);
        AppUser sender = repository.findByUsername(senderUsername);
        List<AppUser> newRequests = recipient.getRequests();
        newRequests.remove(sender);
        List<AppUser> recipientFriends = recipient.getFriends();
        List<AppUser> senderFriends = sender.getFriends();
        recipientFriends.add(sender);
        senderFriends.add(recipient);
        recipient.setFriends(recipientFriends);
        sender.setFriends(senderFriends);
        repository.save(sender);
        repository.save(recipient);
        return new ResponseEntity<>(sender, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AppUser> declineFriendRequest(Long recipientId, String senderUsername) {
        AppUser recipient = repository.findByAppUserID(recipientId);
        AppUser sender = repository.findByUsername(senderUsername);
        List<AppUser> requests = recipient.getRequests();
        requests.remove(sender);
        recipient.setRequests(requests);
        repository.save(recipient);
        return new ResponseEntity<>(sender, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Post> createPost(Long userId, Post post) {
        AppUser appUser = repository.findByAppUserID(userId);

        if (appUser == null) {
            // If user not found, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }

        // Set the user for the post
        post.setAppUser(appUser);

        // Save the post to the user's list of posts
        List<Post> userPosts = appUser.getPosts();
        userPosts.add(post);
        appUser.setPosts(userPosts);

        // Save the updated user to the database
        repository.save(appUser);

        // Return the created post with a 201 Created response
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }



    //POINTLESS WITH FRONTEND
    @Override
    //to get other people's posts
    public ResponseEntity<List<Post>> getPostsByUserId(Long userId, Long friendId) {
        AppUser appUser = repository.findByAppUserID(userId);
        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }
        AppUser targetUser = repository.findByAppUserID(friendId);
        if (targetUser == null) {
            return ResponseEntity.notFound().build();
        }

        List<AppUser> friends = appUser.getFriends();
        boolean isFriend = false;
        for (AppUser friend : friends) {
            if (friend.getAppUserID().equals(friendId)) {
                isFriend = true;
                break;
            }
        }

        if (!isFriend) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(targetUser.getPosts());
    }

    @Override
    public ResponseEntity<List<Post>> getPostsByUsername(Long userId, String targetUsername) {
        AppUser appUser = repository.findByAppUserID(userId);

        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }

        AppUser targetUser = repository.findByUsername(targetUsername);

        if (targetUser == null) {
            return ResponseEntity.notFound().build();
        }

        List<AppUser> friends = appUser.getFriends();
        boolean isFriend = false;
        for (AppUser friend : friends) {
            if (friend.getUsername().equals(targetUsername)) {
                isFriend = true;
                break;
            }
        }

        boolean isSelf = false;

        //check the database to see if it is yourself
        if(targetUser.getAppUserID() == appUser.getAppUserID()){
            isSelf = true;
        }






        if (!isFriend && !isSelf) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(targetUser.getPosts());
    }



    @Override
    public ResponseEntity<List<Message>> getMessagesByUsername(Long userId, String targetUsername) {
        AppUser appUser = repository.findByAppUserID(userId);

        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }

        AppUser targetUser = repository.findByUsername(targetUsername);

        if (targetUser == null) {
            return ResponseEntity.notFound().build();
        }


        List<AppUser> friends = appUser.getFriends();
        boolean isFriend = false;
        for (AppUser friend : friends) {
            if (friend.getUsername().equals(targetUsername)) {
                isFriend = true;
                break;
            }
        }
        boolean isSelf = false;

        //check the database to see if it is yourself
        if(targetUser.getAppUserID() == appUser.getAppUserID()){
            isSelf = true;
        }

        if (!isFriend && !isSelf) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(targetUser.getMessages());
    }

    }
