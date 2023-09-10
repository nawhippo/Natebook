package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "comments")
public class Comment {

    public Comment() {
        reactions = new HashMap<>();
        reactionIds = reactions.keySet();
        reactionStrings = reactions.values();
    }

    @Transient
    public HashMap<Long, String> reactions;
    public Set<Long> reactionIds;
    public Collection<String> reactionStrings;



    public HashMap<Long, String> getReactions() {
        return reactions;
    }

    public void setReactions(HashMap<Long, String> reactions) {
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

    //for display purposes
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



    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
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

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Post post;



    @OneToOne
    @JoinColumn(name ="user_id")
    @JsonIgnore
    private AppUser appUser;
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

    @Column
    private String commenterusername;

    public void addReaction(Long userId, String action) {
        String existingReaction = reactions.put(userId, action);

        if ("Like".equals(action)) {
            likesCount++;
        } else if ("Dislike".equals(action)) {
            dislikesCount++;
        }

        if (existingReaction != null) {
            if ("Like".equals(existingReaction)) {
                likesCount--;
            } else if ("Dislike".equals(existingReaction)) {
                dislikesCount--;
            }
        }
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
