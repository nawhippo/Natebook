package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.models.UserDTO;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;


//intermediary between model and database (SoloProject.SocialMediaApp.repository)
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AppUserServiceImpl(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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



    //your own message

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
    public ResponseEntity<List<Long>> getFriends(Long userId) {
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
        //find user, but with validation step
        if (appUser == null) {
            return ResponseEntity.notFound().build();
        } else {
            for (Long friend : appUser.getFriends()) {
                if (friend == friendId) {
                    return findByAppUserID(friend);
                }
            }
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<AppUser> getFriend(Long userId, String friendUsername) {
    AppUser user = repository.findByAppUserID(userId);
    for(Long friend : user.getFriends()){
        if(repository.findByAppUserID(friend).getUsername().equals(friendUsername)){
            return ResponseEntity.ok(repository.findByUsername(friendUsername));
        }
    }
    return ResponseEntity.notFound().build();
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
    public ResponseEntity<List<Long>> getAllFriendRequests(Long UserId){
        AppUser user = repository.findByAppUserID(UserId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.getRequests());
    }


    @Override
    public ResponseEntity<List<UserDTO>> getAllFriendsDTOS(Long UserId){
        AppUser user = repository.findByAppUserID(UserId);
        List<Long> friends = user.getFriends();
        List<UserDTO> DTOList = convertToDTOList(friends);
        return ResponseEntity.ok(DTOList);
    }

    private List<UserDTO> convertToDTOList(List<Long> ids) {
        List<UserDTO> result = new ArrayList<>();
        for (long id : ids){
        result.add(convertToDTO(id));
        }
        return result;
    }

    private UserDTO convertToDTO(Long id){
        AppUser user = repository.findByAppUserID(id);
        return new UserDTO(
                id, user.getUsername(), user.getFirstname(), user.getLastname(), user.getPosts(), user.getMessages()
        );
    }


    @Override
    public ResponseEntity<List<UserDTO>> getAllFriendRequestsDTOS(Long UserId){
        AppUser user = repository.findByAppUserID(UserId);
        List<Long> friendreqs = user.getRequests();
        List<UserDTO> DTOList = convertToDTOList(friendreqs);
        return ResponseEntity.ok(DTOList);
    }

    //strings are usernames
    @Override
    public ResponseEntity<AppUser> sendFriendRequest(Long senderId, Long friendId) {
        AppUser friend = repository.findByAppUserID(friendId);
        List<Long> newRequests = friend.getRequests();
        newRequests.add(senderId);
        friend.setRequests(newRequests);
        repository.save(friend);
        return new ResponseEntity<>(friend, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AppUser> sendFriendRequest(Long senderId, String recipientUsername) {
        AppUser recipient = repository.findByUsername(recipientUsername);
        List<Long> newRequests = recipient.getRequests();
        newRequests.add(senderId);
        recipient.setRequests(newRequests);
        repository.save(recipient);
        return new ResponseEntity<>(recipient, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<AppUser> acceptFriendRequest(Long recipientId, Long senderId) {
        AppUser recipient = repository.findByAppUserID(recipientId);
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

    @Override
    public ResponseEntity<AppUser> declineFriendRequest(Long recipientId, Long senderId) {
        AppUser appUser = repository.findByAppUserID(recipientId);
        List<Long> requests = appUser.getRequests();
        requests.remove(senderId);
        appUser.setRequests(requests);
        repository.save(appUser);
        return new ResponseEntity<>(appUser, HttpStatus.OK);
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

        List<Long> friends = appUser.getFriends();
        boolean isFriend = false;
        for (Long friend : friends) {
            if (friend == (friendId)) {
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

        boolean isSelf = targetUser.getAppUserID().equals(appUser.getAppUserID());

        if (!isSelf) {
            List<Long> friends = appUser.getFriends();
            boolean isFriend = false;
            for (Long friend : friends) {
                if (friend == targetUser.getAppUserID()) {
                    isFriend = true;
                    break;
                }
            }

            if (!isFriend) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
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


        List<Long> friends = appUser.getFriends();
        boolean isFriend = false;
        for (Long friend : friends) {
            if (friend == targetUser.getAppUserID()) {
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


    @Override
    public ResponseEntity<List<AppUser>> getAllUsers(){
        List<AppUser> allUsers = repository.findAll();
        return ResponseEntity.ok(allUsers);
    }

    @Override
    public ResponseEntity<AppUser> getAccountDetails(Long userid){
        AppUser user = repository.findByAppUserID(userid);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<AppUser> updateAccountDetails(Long userId, String newFirstName, String newLastName, String newEmail) {
        AppUser user = repository.findByAppUserID(userId);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Update the user's account details if the new values are provided
        if (newFirstName != null) {
            user.setFirstname(newFirstName);
        }
        if (newLastName != null) {
            user.setLastname(newLastName);
        }
        if (newEmail != null) {
            user.setEmail(newEmail);
        }

        //save the updated user to the database
        repository.save(user);

        return ResponseEntity.ok(user);
    }


    public AppUser authenticate(String username, String password) throws AuthenticationException {
        AppUser user = repository.findByUsername(username);


        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
        return user;
    }


    public ResponseEntity<AppUser> removeFriend(Long userId, String username){
        AppUser user = repository.findByAppUserID(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<Long> friends = user.getFriends();
        AppUser friend = repository.findByUsername(username);

        if(friend == null){
            return ResponseEntity.notFound().build();
        }
        friends.remove(friend);
        user.setFriends(friends);
        repository.save(user);
        return ResponseEntity.ok(user);
    }



    //to display user data extrapolated from longs.
    private UserDTO convertToUserDTO(Long userId) {
        AppUser appUser = repository.findByAppUserID(userId);
        return new UserDTO(
                appUser.getAppUserID(),
                appUser.getUsername(),
                appUser.getFirstname(),
                appUser.getLastname(),
                appUser.getPosts(),
                appUser.getMessages()
        );
    }



}
