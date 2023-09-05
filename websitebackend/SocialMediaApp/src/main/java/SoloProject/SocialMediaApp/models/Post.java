package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "posts")
public class Post {
    public boolean isFriendsonly(){
        return friendsonly;
    }

    public HashMap<Long, String> getReactions() {
        return reactions;
    }

    public void setReactions(HashMap<Long, String> reactions) {
        this.reactions = reactions;
    }


    @Transient
    public HashMap<Long, String> reactions;
    public void setFriendsonly(boolean friendsonly) {
        this.friendsonly = friendsonly;
    }

    public boolean friendsonly;
    public Long getPosterid() {
        return posterid;
    }

    public void setPosterid(Long posterid) {
        this.posterid = posterid;
    }

    public Post(Long id, AppUser appUser, String posterusername, String title, String description, List<Comment> commentList, Long posterid, boolean friendsonly) {
        this.id = id;
        this.appUser = appUser;
        this.posterusername = posterusername;
        this.title = title;
        this.description = description;

        this.commentList = new ArrayList<>();
        this.dateTime =  getDateTimeAsString();
        this.posterid = posterid;
        this.friendsonly = friendsonly;
    }

    public Post() {

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

    //for display
    private int likesCount;

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

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