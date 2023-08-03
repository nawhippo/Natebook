package SoloProject.SocialMediaApp.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser sender;

    @ManyToMany
    @JoinTable(
            name = "message_recipients",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<AppUser> recipients;

    @Column
    private Date dateTime; // New field for date/time

    public Message(Long id, String content, AppUser sender, List<AppUser> recipients) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.recipients = recipients;
    }

    public Message() {
    }

    public Long getId() {
        return id;
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
}