package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.models.Comment;
import SoloProject.SocialMediaApp.models.Message;
import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final AppUserRepository repository;


    public PostService(AppUserRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<List<Post>> getWebsitePosts(){
        List<AppUser> users = repository.findAll();
        List<Post> websiteposts = new ArrayList<>();
        for(AppUser user : users){
            List<Post> userposts = user.getPosts();
                for(Post post : userposts){
                    if(!post.isFriendsonly()){
                        websiteposts.add(post);
                    }
                }
        }
        return ResponseEntity.ok(websiteposts);
    }

    public ResponseEntity<Post> getPostById(Long userId, Long postId, Long id) {
        AppUser appUser = repository.findByAppUserID(userId);
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

    public ResponseEntity<Post> createPost(Long userId, Post post) {
        AppUser appUser = repository.findByAppUserID(userId);

        if (appUser == null) {
            return ResponseEntity.notFound().build();
        }
        String username = appUser.getUsername();
        post.setAppUser(appUser);
        post.setPosterusername(username);
        post.setPosterid(appUser.getAppUserID());
        List<Post> userPosts = appUser.getPosts();
        userPosts.add(post);
        appUser.setPosts(userPosts);
        repository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }



    public ResponseEntity<List<Post>> getAllPosts(Long userId) {
        AppUser appUser = repository.findByAppUserID(userId);
        if (appUser != null) {
            return (ResponseEntity<List<Post>>) appUser.getPosts();
        } else {

            return ResponseEntity.notFound().build();
        }
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


    public ResponseEntity<Comment> addDislikeComment(Long userId, Long posterId, Long postId, Long commentId) {
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


    public ResponseEntity<Comment> removeLikeComment(Long userId, Long posterId, Long postId, Long commentId) {
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

    public ResponseEntity<Comment> removeDislikeComment(Long userId, Long posterId, Long postId, Long commentId) {
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


    public ResponseEntity<List<Post>> getAllFriendPosts(Long userId) {
        AppUser appUser = repository.findByAppUserID(userId);
        List<Post> list = new ArrayList<>();
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        for (Long friendid : appUser.getFriends()) {
            AppUser friend = repository.findByAppUserID(friendid);
            list.addAll(friend.getPosts());
        }
        return ResponseEntity.ok(list);
    }


    public ResponseEntity<String> getReactionPost(Long userId, Long posterId, Long postId) {
        AppUser Poster = repository.findByAppUserID(posterId);

        for (Post post : Poster.getPosts()) {
            if (post.getId() == postId) {
                if (post.getLikes().contains(userId)) {
                    return ResponseEntity.ok("Liked");
                }
                if (post.getDislikes().contains(userId)) {
                    return ResponseEntity.ok("Disliked");
                } else {
                    return ResponseEntity.ok("None");
                }
            }
        }
        return (ResponseEntity<String>) ResponseEntity.notFound();
    }

    public ResponseEntity<String> getReactionComment(Long userId, Long posterId, Long postId, Long commentId) {
        AppUser Poster = repository.findByAppUserID(posterId);

        for (Post post : Poster.getPosts()) {
            if (post.getId() == postId) {
                for (Comment comment : post.getCommentList()) {
                    if (commentId == comment.getId()) {
                        if (comment.getLikes().contains(userId)) {
                            return ResponseEntity.ok("Liked");
                        }
                        if (commentId == comment.getId()) {
                            if (comment.getDislikes().contains(userId)) {
                                return ResponseEntity.ok("Disliked");
                            }
                        }
                    }
                }
            }
            return (ResponseEntity<String>) ResponseEntity.notFound();
        }
        return (ResponseEntity<String>) ResponseEntity.notFound();
    }

    public ResponseEntity<Post> deletePost(Long userId, Long postId) {
        AppUser user = repository.findByAppUserID(userId);
        for (Post post : user.getPosts()) {
            if (post.getId() == postId) {
                user.getPosts().remove(post);
                repository.save(user);
                return ResponseEntity.ok(post);
            }
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Post> deleteComment(Long userId, Long postId, Long commentId) {
        AppUser user = repository.findByAppUserID(userId);
        for (Post post : user.getPosts()) {
            if (post.getId() == postId) {
                for (Comment comment : post.getCommentList()) {
                    if (comment.getId() == commentId) {
                        post.removeComment(comment);
                        repository.save(user);
                        return ResponseEntity.ok(post);
                    }
                }
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}