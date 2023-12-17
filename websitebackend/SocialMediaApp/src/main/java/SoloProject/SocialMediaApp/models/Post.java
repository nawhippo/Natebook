package SoloProject.SocialMediaApp.models;

import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "posts")
public class Post {
    private ArrayList<Long> images;

    private boolean isFriendsOnly(){
        return friendsOnly;
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




    public ArrayList<Long> getImages() {
        return images;
    }

    public void setImages(ArrayList<Long> images) {
        this.images = images;
    }

    @ElementCollection
    @MapKeyColumn(name="user_id")
    @Column(name="action")
    @CollectionTable(name="post_reactions", joinColumns=@JoinColumn(name="post_id"))
    private Map<Long, String> reactions = new HashMap<>();


    private void setFriendsOnly(boolean friendsOnly) {
        this.friendsOnly = friendsOnly;
    }

    private boolean friendsOnly;
    public Long getPosterAppUserId() {
        return posterId;
    }

    public void setPosterAppUserId(Long posterAppUserId) {
        this.posterId = posterAppUserId;
    }

    public Post() {
        reactions = new HashMap<>();
    }



    public String getPosterUsername() {
        return posterUsername;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public void setPosterUsername(String posterUsername) {
        this.posterUsername = posterUsername;
    }

    //for display purposes
   private String posterUsername;

    //for display purposes
    private Long posterId;


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

    @Column
    private String dateTime;


    public String getDateTimeAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy 'at' hh:mm:ss a");
        return sdf.format(new Date());
    }

    public Long getId() {
        return id;
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


    public void addImage(Long id){
        this.images.add(id);
    }

}