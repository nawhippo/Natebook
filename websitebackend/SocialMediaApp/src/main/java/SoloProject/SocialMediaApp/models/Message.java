package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "messages")
public class Message {
    public Message() {
    this.dateTime = new Date();
    this.recipients = new ArrayList<>();
    }

    public Message(String content, AppUser sender, boolean isIncoming, List<String> recipients, Date dateTime) {
        this.content = content;
        this.sender = sender;
        this.isIncoming = isIncoming;
        this.recipients = recipients;
        this.dateTime = dateTime;
    }

    public Message(boolean isIncoming) {
        this.isIncoming = isIncoming;
        this.dateTime = new Date();
        this.recipients = new ArrayList<>();
    }

    //message id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String content;


    @ManyToOne
    @JsonBackReference
    private AppUser sender;

    @Column(name = "is_incoming")
    private boolean isIncoming;

   @Column(name = "recipients")
   @ElementCollection
    private List<String> recipients;

    @Column(name = "date_time")
    private Date dateTime;

    public Message(String content, AppUser sender, boolean isIncoming) {
        this.content = content;
        this.sender = sender;
        this.isIncoming = isIncoming;
        this.recipients = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AppUser getSender() {
        return sender;
    }

    public void setSender(AppUser sender) {
        this.sender = sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void addRecipient(String recipient) {
        recipients.add(recipient);
    }

    public void removeRecipient(AppUser recipient) {
        recipients.remove(recipient);
    }

    public void setIncoming(boolean incoming) {
        isIncoming = incoming;
    }

    public boolean isIncoming() {
        return isIncoming;
    }
}