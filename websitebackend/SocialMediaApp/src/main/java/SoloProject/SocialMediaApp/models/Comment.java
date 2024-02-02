package SoloProject.SocialMediaApp.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment {

    public Comment() {
        reactions = new HashMap<>();
    }

    public Long getPostid() {
        return postid;
    }

    public void setPostid(Long postid) {
        this.postid = postid;
    }

    @ElementCollection
    @MapKeyColumn(name="user_id")
    @Column(name="action")
    @CollectionTable(name="comment_reactions", joinColumns=@JoinColumn(name="comment_id"))
    private Map<Long, String> reactions = new HashMap<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private Long postid;

    @Column
    private String commenterusername;


    //for display purposes
    @Column
    Long commenterid;

    @Column
    String dateTime;

    @Column
    private String content;

    private int likesCount;
    @ElementCollection
    private List<Long> dislikes;

    private int dislikesCount;

    public void addReaction(Long userId, String action) {
        System.out.println("Before Add: Likes: " + likesCount + ", Dislikes: " + dislikesCount);

        String existingReaction = reactions.getOrDefault(userId, "None");

        System.out.println("Existing Reaction: " + existingReaction);

        if (!existingReaction.equals(action)) {
            //Handle new reaction
            if ("Like".equals(action)) {
                likesCount++;
            } else if ("Dislike".equals(action)) {
                dislikesCount++;
            }

            //Handle existing reaction
            if ("Like".equals(existingReaction)) {
                likesCount--;
            } else if ("Dislike".equals(existingReaction)) {
                dislikesCount--;
            }

            //Update the reaction in the map
            reactions.put(userId, action);
        }

        System.out.println("After Add: Likes: " + likesCount + ", Dislikes: " + dislikesCount);
    }

    public void removeReaction(Long userId) {
        String existingReaction = reactions.remove(userId);

        if (existingReaction != null) {
            if ("Like".equals(existingReaction)) {
                likesCount--;
            } else if ("Dislike".equals(existingReaction)) {
                dislikesCount--;
            }
        }
    }
}
