package SoloProject.SocialMediaApp.models;


import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "notification_type", nullable = false)
    private String notificationType;


    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "thread_id")
    private Long threadId;

    public Notification() {
    }

    public Notification(Long userId, String notificationType, Long objectId) {
        this.userId = userId;
        this.notificationType = notificationType;
        this.objectId = objectId;
    }

    public Notification(Long userId, String notificationType, Long objectId, Long threadId) {
        this.userId = userId;
        this.notificationType = notificationType;
        this.objectId = objectId;
        this.threadId = threadId;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public Long getObjectId() {
        return this.objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }
}