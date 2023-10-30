package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.Comment;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.repository.CommentRepository;
import SoloProject.SocialMediaApp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {

    private final AppUserRepository appUserRepository;
    private final PostRepository postRepository;

    private final CommentRepository commentRepository;


    @Autowired
    public CommentService(AppUserRepository repository, PostRepository postRepository, CommentRepository commentRepository) {
        this.appUserRepository = repository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Long PostId, Comment comment) {
        comment.setPostid(PostId);
        return commentRepository.save(comment);
    }


    public Comment handleCommentReaction(Long reactorId, Long commentId, String action) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (!optionalComment.isPresent()) {
            return null;
        }

        Comment comment = optionalComment.get();
        Map<Long, String> reactions = comment.getReactions();
        String existingReaction = reactions.getOrDefault(reactorId, "None");

        if (existingReaction.equals(action)) {
            return commentRepository.save(comment);
        } else {
            if (!existingReaction.equals("None")) {
                comment.removeReaction(reactorId);
            }
            comment.addReaction(reactorId, action);
        }

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment deleteComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (!optionalComment.isPresent()) {
            // Handle the case where the comment is not found.
            // You could throw an exception or return null here.
            return null;
        }

        Comment comment = optionalComment.get();
        commentRepository.delete(comment);
        return comment;
    }
}
