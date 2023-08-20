package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.util.List;

public class Comment {
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
    @Column
    private List<Long> likes;
    @Column
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
