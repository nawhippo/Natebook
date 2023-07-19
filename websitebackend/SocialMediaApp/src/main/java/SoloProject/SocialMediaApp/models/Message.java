package SoloProject.SocialMediaApp.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "messages")
public class Message {

    @OneToMany
    private List<AppUser> recipients;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser sender;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;

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


    public AppUser getSender() {
        return sender;
    }


    public List<AppUser> getRecipients() {
        return recipients;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(AppUser appUser) {
        this.sender = appUser;
    }


    public void setRecipients(List<AppUser> recipients) {
        this.recipients = recipients;
    }
}
