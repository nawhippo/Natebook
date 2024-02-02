package SoloProject.SocialMediaApp.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
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
}