package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
        return commenterusername;
    }

    public void setCommenterusername(String commenterusername) {
        this.commenterusername = commenterusername;
    }

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;



    @OneToOne
    @JoinColumn(name ="user_id")
    @JsonIgnore
    private AppUser appUser;
    @Column
    private String content;
    @ElementCollection
    private List<Long> likes;
    @ElementCollection
    private List<Long> dislikes;
    @Column
    private String commenterusername;

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
