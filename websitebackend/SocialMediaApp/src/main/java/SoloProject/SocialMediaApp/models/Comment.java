package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Comment {
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

    public List<Long> getLikes() {
        return likes;
    }

    public void setLikes(List<Long> likes) {
        this.likes = likes;
    }

    public List<Long> getDislikes() {
        return dislikes;
    }

    public void setDislikes(List<Long> dislikes) {
        this.dislikes = dislikes;
    }

    public String getCommenterusername() {
        return commenterUsername;
    }

    public void setCommenterusername(String commenterusername) {
        this.commenterUsername = commenterusername;
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
    @ElementCollection
    private List<Long> likes;

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
    private String commenterUsername;

    public void addLike(Long userId) {
        this.likes.add(userId);
    }

    public void addDislike(Long userId) {
        this.dislikes.add(userId);
    }

    public void removeLike(Long userId) {
        this.likes.remove(userId);
    }

    public void removeDislike(Long userId) {
        this.dislikes.remove(userId);
    }
}
