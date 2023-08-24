package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.*;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//intermediary between model and database (SoloProject.SocialMediaApp.repository)
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository repository;

    @PersistenceContext
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    public AppUserServiceImpl(AppUserRepository repository, PasswordEncoder passwordEncoder, EntityManager entityManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }


    @Override
    public AppUser createUser(String firstName, String lastName, String username, String email, String password) {
        if (repository.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username is already taken. Please choose a different username.");
        }
        List<Message> messages = new ArrayList<>();
        AppUser newuser = new AppUser(messages, firstName, lastName, username, email, password);
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
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(appUsers);
    }


    //Post functions
    @Override
    public ResponseEntity<Post> getPostById(Long userId, Long postId, Long id) {
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
    public ResponseEntity<Message> getMessageById(Long userId, Long messageId) {
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
        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }
        List<Message> messages = appUser.getMessages();
        return ResponseEntity.ok(messages);
    }

    @Override
    @Transactional
    public ResponseEntity<Message> sendMessage(Long senderId, String content, List<String> recipientNames) {
        AppUser sender = repository.findByAppUserID(senderId);
        Message message = new Message(content, sender, false);

        for (String recipientName : recipientNames) {
            message.addRecipient(recipientName);
        }

        for (String recipientName : recipientNames) {
            AppUser recipient = repository.findByUsername(recipientName);
            recipient.getMessages().add(message);
            repository.save(recipient);
        }
        message.setIncoming(true);
        sender.getMessages().add(message);
        repository.save(sender);

        return ResponseEntity.ok(message);
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
        for (Long friend : user.getFriends()) {
            if (repository.findByAppUserID(friend).getUsername().equals(friendUsername)) {
                return ResponseEntity.ok(repository.findByUsername(friendUsername));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<Long>> getAllFriendRequests(Long UserId) {
        AppUser user = repository.findByAppUserID(UserId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.getRequests());
    }


    @Override
    public ResponseEntity<List<UserDTO>> getAllFriendsDTOS(Long UserId) {
        AppUser user = repository.findByAppUserID(UserId);
        if (user != null && user.getFriends() != null) {
            List<Long> friends = user.getFriends();
            List<UserDTO> DTOList = convertToDTOList(friends);
            return ResponseEntity.ok(DTOList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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


    @Override
    public ResponseEntity<List<UserDTO>> getAllFriendRequestsDTOS(Long UserId) {
        AppUser user = repository.findByAppUserID(UserId);
        List<Long> friendreqs = user.getRequests();
        List<UserDTO> DTOList = convertToDTOList(friendreqs);
        for (UserDTO dto : DTOList) {
            System.out.println(dto);
        }
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

        if (recipient.getRequests() == null) {
            recipient.setRequests(new ArrayList<>());
        }

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
            return ResponseEntity.notFound().build();
        }
        String username = appUser.getUsername();
        post.setAppUser(appUser);
        post.setPosterusername(username);
        List<Post> userPosts = appUser.getPosts();
        userPosts.add(post);
        appUser.setPosts(userPosts);
        repository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }


    @Override
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


        if (targetUser.getAppUserID() == appUser.getAppUserID()) {
            isSelf = true;
        }

        if (!isFriend && !isSelf) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Message> messages = new ArrayList<>();
        List<Message> incoming = appUser.getMessages();


        for (Message message : incoming) {
            if (message.getSender().equals(targetUsername)) {
                messages.add(message);
            }
        }

        return ResponseEntity.ok(messages);
    }


    @Override
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> allUsers = repository.findAll();
        return ResponseEntity.ok(allUsers);
    }

    @Override
    public ResponseEntity<AppUser> getAccountDetails(Long userid) {
        AppUser user = repository.findByAppUserID(userid);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<AppUser> updateAccountDetails(Long userId, String newFirstName, String newLastName, String newEmail, String newPassword) {
        AppUser user = repository.findByAppUserID(userId);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (newFirstName != null) {
            user.setFirstname(newFirstName);
        }
        if (newLastName != null) {
            user.setLastname(newLastName);
        }
        if (newEmail != null) {
            user.setEmail(newEmail);
        }

        if (newPassword != null) {
            user.setPassword(newPassword);
        }
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


    public ResponseEntity<AppUser> removeFriend(Long userId, String username) {
        AppUser user = repository.findByAppUserID(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<Long> friends = user.getFriends();
        AppUser friend = repository.findByUsername(username);

        if (friend == null) {
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
                appUser.getEmail()
        );
    }


    public ResponseEntity<?> createComment(Long userId, Long postId, Comment comment) {
        AppUser appUser = repository.findByAppUserID(userId);

        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<Post> postOptional = appUser.getPosts().stream()
                .filter(post -> post.getId().equals(postId))
                .findFirst();

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            comment.setCommenterusername(appUser.getUsername());
            post.addComment(comment);
            repository.save(appUser);
            //have to attach the new post to the user.
            return ResponseEntity.status(HttpStatus.CREATED).body(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //how to get specific post, without poster's userid? add posterid parameter
    public ResponseEntity<Post> addLikePost(Long userId, Long posterId, Long postId) {
        //find by user
        AppUser poster = repository.findByAppUserID(posterId);
        if (poster != null) {
            for (Post post : poster.getPosts()) {
                //post found
                if (postId == post.getId()) {
                    if (!post.getLikes().contains(userId) && !post.getDislikes().contains(userId)) {
                        post.addLike(userId);
                        post.setLikesCount(post.getLikes().size());
                        return ResponseEntity.ok(post);
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public ResponseEntity<Post> addDislikePost(Long userId, Long posterId, Long postId) {
        //find by user
        AppUser poster = repository.findByAppUserID(posterId);
        if (poster != null) {
            for (Post post : poster.getPosts()) {
                //post found
                if (postId == post.getId()) {
                    if (!post.getLikes().contains(userId) && !post.getDislikes().contains(userId)) {
                        post.addDislike(userId);
                        post.setDislikesCount(post.getDislikes().size());
                        return ResponseEntity.ok(post);
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    public ResponseEntity<Post> removeLikePost(Long userId, Long posterId, Long postId) {
        //find by user
        AppUser poster = repository.findByAppUserID(posterId);
        if (poster != null) {
            for (Post post : poster.getPosts()) {
                //post found
                if (postId == post.getId()) {
                    if (post.getLikes().contains(userId)) {
                        post.removeLike(userId);
                        post.setLikesCount(post.getLikes().size());
                        return ResponseEntity.ok(post);
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public ResponseEntity<Post> removeDislikePost(Long userId, Long posterId, Long postId) {
        //find by user
        AppUser poster = repository.findByAppUserID(posterId);
        if (poster != null) {
            for (Post post : poster.getPosts()) {
                //post found
                if (postId == post.getId()) {
                    if (post.getDislikes().contains(userId)) {
                        post.removeDislike(userId);
                        post.setDislikesCount(post.getDislikes().size());
                        return ResponseEntity.ok(post);
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



    public ResponseEntity<Comment> addLikeComment(Long userId, Long posterId, Long postId, Long commentId) {
        //find by user
        AppUser poster = repository.findByAppUserID(posterId);
        if (poster != null) {
            for (Post post : poster.getPosts()) {
                //post found
                if (postId == post.getId()) {
                    for (Comment comment : post.getCommentList()) {
                        if (comment.getId() == commentId) {
                            if (!comment.getLikes().contains(userId) && comment.getDislikes().contains(userId)) {
                                comment.addLike(userId);
                                return ResponseEntity.ok(comment);
                            }
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                        }
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }




        public ResponseEntity<Comment> addDislikeComment(Long userId, Long posterId, Long postId, Long commentId)  {
            //find by user
            AppUser poster = repository.findByAppUserID(posterId);
            if (poster != null) {
                for (Post post : poster.getPosts()) {
                    //post found
                    if (postId == post.getId()) {
                        for (Comment comment : post.getCommentList()) {
                            if (comment.getId() == commentId) {
                                if (!comment.getLikes().contains(userId) && comment.getDislikes().contains(userId)) {
                                    comment.addDislike(userId);
                                    return ResponseEntity.ok(comment);
                                }
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                            }
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                        }
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


    public ResponseEntity<Comment> removeLikeComment(Long userId, Long posterId, Long postId, Long commentId)  {
        //find by user
        AppUser poster = repository.findByAppUserID(posterId);
        if (poster != null) {
            for (Post post : poster.getPosts()) {
                //post found
                if (postId == post.getId()) {
                    for (Comment comment : post.getCommentList()) {
                        if (comment.getId() == commentId) {
                            if (!comment.getLikes().contains(userId)) {
                                comment.removeLike(userId);
                            }
                        }
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public ResponseEntity<Comment> removeDislikeComment(Long userId, Long posterId, Long postId, Long commentId)  {
        //find by user
        AppUser poster = repository.findByAppUserID(posterId);
        if (poster != null) {
            for (Post post : poster.getPosts()) {
                //post found
                if (postId == post.getId()) {
                    for (Comment comment : post.getCommentList()) {
                        if (comment.getId() == commentId) {
                            if (!comment.getDislikes().contains(userId)) {
                                comment.removeDislike(userId);
                            }
                        }
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }





}