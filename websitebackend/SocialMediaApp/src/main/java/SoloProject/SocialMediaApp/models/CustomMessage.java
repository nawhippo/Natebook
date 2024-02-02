package SoloProject.SocialMediaApp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;


@Entity
@Getter
@Setter
public class CustomMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String body;
    private long senderId;
    private long groupChatId;
    private long originalMessageId;

    private String senderFirstname;
    private String senderLastname;

    private ArrayList<Long> participants;
    private Date sent;

    public CustomMessage() {
      this.sent = new Date();
    }


    public CustomMessage(Long id, String title, String body, long senderId, long groupChatId, long originalMessageId, String senderFirstname, String senderLastname, ArrayList<Long> participants) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.senderId = senderId;
        this.groupChatId = groupChatId;
        this.originalMessageId = originalMessageId;
        this.senderFirstname = senderFirstname;
        this.senderLastname = senderLastname;
        this.sent = new Date();
        this.participants = participants;
    }

}
