package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    public Long getPosterid() {
        return posterid;
    }

    public void setPosterid(Long posterid) {
        this.posterid = posterid;
    }

    public Post(Long id, AppUser appUser, String posterusername, String title, String description, List<Long> likes, List<Long> dislikes, List<Comment> commentList, Long posterid) {
        this.id = id;
        this.appUser = appUser;
        this.posterusername = posterusername;
        this.title = title;
        this.description = description;
        this.likes = new ArrayList<>();
        this.dislikes = new ArrayList<>();
        this.commentList = new ArrayList<>();
        this.dateTime =  getDateTimeAsString();
        this.posterid = posterid;
    }

    public Post() {

    }

    public List<Long> addLike(Long userId){
        likes.add(userId);
        return likes;
    }

    public List<Long> addDislike(Long userId){
        dislikes.add(userId);
        return dislikes;
    }
    public List<Long> removeLike(Long userId){
        likes.remove(userId);
        return likes;
    }

    public List<Long> removeDislike(Long userId){
        dislikes.remove(userId);
        return dislikes;
    }



    public String getPosterusername() {
        return posterusername;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;




    @ManyToOne
    @JoinColumn(name ="user_id")
    @JsonIgnore
    private AppUser appUser;

    public void setPosterusername(String posterusername) {
        this.posterusername = posterusername;
    }

    //for display purposes
   private String posterusername;

    //for display purposes
    private Long posterid;

    @Column
    private String title;

    @Column
    private String description;


    //return the length of this to get number of.

    @ElementCollection
    private List<Long> likes;


    //for display
    private int likesCount;

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }


    @ElementCollection
    private List<Long> dislikes;

    private int dislikesCount;

    public int getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> commentList;

    public void addComment(Comment comment) {
        commentList.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment){
        commentList.remove(comment);
        comment.setPost(null);
    }


    @Column
    private String dateTime;


    public String getDateTimeAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss a");
        return sdf.format(new Date());
    }

    public Long getId() {
        return id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}