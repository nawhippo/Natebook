package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.Comment;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import SoloProject.SocialMediaApp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;




    @PutMapping("/comment/{commentId}/{reactorId}/updateReactionComment")
    public ResponseEntity<Comment> updateCommentReaction(
            @PathVariable Long reactorId,
            @PathVariable Long commentId,
            @RequestBody Map<String, String> payload) {

        String action = payload.get("action");
        Comment updatedComment = commentService.handleCommentReaction(reactorId, commentId, action);

        if (updatedComment != null) {
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/comment/{postId}/createComment")
    public ResponseEntity<?> createComment(
            @PathVariable Long postId,
            @RequestBody Comment comment
    ) {
        comment.setCommenterusername(comment.getCommenterusername());
        Comment createdComment = commentService.createComment(postId, comment);

        if (createdComment != null) {
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Could not create comment", HttpStatus.BAD_REQUEST);
        }
    }



    @DeleteMapping("/comment/{commentId}/deleteComment")
    public ResponseEntity<Comment> deleteComment(
            @PathVariable String username,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        Comment deletedComment = commentService.deleteComment(commentId);

        if (deletedComment != null) {
            return new ResponseEntity<>(deletedComment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}