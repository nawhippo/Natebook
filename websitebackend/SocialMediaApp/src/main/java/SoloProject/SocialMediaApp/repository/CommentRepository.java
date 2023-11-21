package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findBypostid(Long postId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.commenterid = ?1")
    void deleteByCommenterId(Long commenterId);

    List<Comment> findByPostId(Long postId);
}