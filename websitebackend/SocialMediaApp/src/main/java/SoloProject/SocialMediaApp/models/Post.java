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

    public Map<Long, String> getReactions() {
        return reactions;
    }

    public void setReactions(Map<Long, String> reactions) {
        this.reactions = reactions;
    }

    public Set<Long> getReactionIds() {
        if(reactions != null) {
            return reactions.keySet();
        }
        return Collections.emptySet();
    }

    public Collection<String> getReactionStrings() {
        if(reactions != null) {
            return reactions.values();
        }
        return Collections.emptyList();
    }





    @ElementCollection
    @MapKeyColumn(name="user_id")
    @Column(name="action")
    @CollectionTable(name="post_reactions", joinColumns=@JoinColumn(name="post_id"))
    private Map<Long, String> reactions = new HashMap<>();


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

    public Post() {
        reactions = new HashMap<>();
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


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
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
        String existingReaction = reactions.getOrDefault(userId, "None");
        if (!existingReaction.equals(action)) {
            if ("Like".equals(action)) {
                likesCount++;
            } else if ("Dislike".equals(action)) {
                dislikesCount++;
            }

            if ("Like".equals(existingReaction)) {
                likesCount--;
            } else if ("Dislike".equals(existingReaction)) {
                dislikesCount--;
            }

            reactions.put(userId, action);
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