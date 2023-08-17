package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "messages")
public class Message {
    public Message(String content, AppUser sender, List<AppUser> recipients, Date dateTime) {
        this.content = content;
        this.sender = sender;
        this.recipients = recipients;
        this.dateTime = dateTime;
    }

    public Message() {
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
    @JoinColumn(name = "sender_id")
    @JsonBackReference
    private AppUser sender;

    @ManyToMany
    @JoinTable(
            name = "message_recipients",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "recipient_id")
    )
    private List<AppUser> recipients;

    @Column(name = "date_time")
    private Date dateTime;

    public Message(String content, AppUser sender) {
        this.content = content;
        this.sender = sender;
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

    public List<AppUser> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<AppUser> recipients) {
        this.recipients = recipients;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void addRecipient(AppUser recipient) {
        recipients.add(recipient);
    }

    public void removeRecipient(AppUser recipient){
        recipients.remove(recipient);
    }
}