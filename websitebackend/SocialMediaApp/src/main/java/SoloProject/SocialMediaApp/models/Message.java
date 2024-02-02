package SoloProject.SocialMediaApp.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //appuserid
    Long senderid;


    @CreationTimestamp
    private LocalDateTime timestamp;

    //threadid;
    Long threadid;

    String content;


    public Message(Long senderid, Long threadid, String content) {
        this.senderid = senderid;
        this.threadid = threadid;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{id=" + this.id + ", content='" + this.content + "', sender='" + this.senderid + "', ...other fields...}";
    }
}