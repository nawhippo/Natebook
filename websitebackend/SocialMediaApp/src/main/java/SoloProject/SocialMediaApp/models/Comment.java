package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;

@Entity
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



    public Map<Long, String> getReactions() {
        return reactions;
    }

    public void setReactions(Map<Long, String> reactions) {
        this.reactions = reactions;
    }

    public String getCommenterusername() {
        return commenterusername;
    }

    public void setCommenterusername(String commenterusername) {
        this.commenterusername = commenterusername;
    }

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



    public Long getCommenterid() {
        return commenterid;
    }

    public void setCommenterid(Long commenterid) {
        this.commenterid = commenterid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Long> getDislikes() {
        return dislikes;
    }

    public void setDislikes(List<Long> dislikes) {
        this.dislikes = dislikes;
    }

    @Column
    private String content;

    private int likesCount;
    @ElementCollection
    private List<Long> dislikes;

    private int dislikesCount;

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public void addReaction(Long userId, String action) {
        System.out.println("Before Add: Likes: " + likesCount + ", Dislikes: " + dislikesCount);

        String existingReaction = reactions.getOrDefault(userId, "None");

        System.out.println("Existing Reaction: " + existingReaction);

        if (!existingReaction.equals(action)) {
            // Handle new reaction
            if ("Like".equals(action)) {
                likesCount++;
            } else if ("Dislike".equals(action)) {
                dislikesCount++;
            }

            // Handle existing reaction
            if ("Like".equals(existingReaction)) {
                likesCount--;
            } else if ("Dislike".equals(existingReaction)) {
                dislikesCount--;
            }

            // Update the reaction in the map
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
