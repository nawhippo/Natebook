package SoloProject.SocialMediaApp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.Date;


@Entity
public class CustomMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getGroupChatId() {
        return groupChatId;
    }

    public void setGroupChatId(long groupChatId) {
        this.groupChatId = groupChatId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long sender) {
        this.senderId = sender;
    }

    public long getOriginalMessageId() {
        return originalMessageId;
    }

    public void setOriginalMessageId(long originalMessageId) {
        this.originalMessageId = originalMessageId;
    }

    private String title;

    private String body;
    private long senderId;
    private long groupChatId;
    private long originalMessageId;

    private String senderFirstname;
    private String senderLastname;

    private ArrayList<Long> participants;

    public ArrayList<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Long> participants) {
        this.participants = participants;
    }

    private Date sent;

    public String getSenderFirstname() {
        return senderFirstname;
    }

    public void setSenderFirstname(String senderFirstname) {
        this.senderFirstname = senderFirstname;
    }

    public String getSenderLastname() {
        return senderLastname;
    }

    public void setSenderLastname(String senderLastname) {
        this.senderLastname = senderLastname;
    }



    public Date getSent() {
        return sent;
    }

    public CustomMessage() {
      this.sent = new Date();
    }

    public void setSent(Date sent) {
        this.sent = sent;
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
