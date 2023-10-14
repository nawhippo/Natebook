package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    //for retrieval of list of post for an appuser
    List<Post> findByPosterId(Long posterId);
    List<Post> findByPosterIdAndFriendsOnlyTrue(Long posterId);
    List<Post> findByFriendsOnlyFalse();
    List<Post> findByPosterIdAndFriendsOnlyFalse(Long posterAppUserId);
    List<Post> findByPosterUsernameAndFriendsOnlyTrue(String posterUsername);
    List<Post> findByPosterUsernameAndFriendsOnlyFalse(String posterUsername);
    List<Post> findByPosterUsername(String posterUsername);


    @Transactional
    @Modifying
    void deleteByPosterId(Long posterId);
}