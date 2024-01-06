package SoloProject.SocialMediaApp.models;

import com.google.cloud.Identity;
import jakarta.persistence.*;

@Entity
@Table(name = "follows")
public class FollowRelationship {
    public FollowRelationship(Long id) {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowedId() {
        return followedId;
    }

    public void setFollowedId(Long followedId) {
        this.followedId = followedId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private Long followerId;

    @Column
    private Long followedId;

    public FollowRelationship(Long id, Long followerId, Long followedId) {
        this.id = id;
        this.followerId = followerId;
        this.followedId = followedId;
    }
}
