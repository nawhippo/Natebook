package models;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Message {

    @OneToMany
    private List<User> recipients;

    @OneToOne
    private User sender;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;

    public Message(Long id, String content, User sender, List<User> recipients) {
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


    public User getSender() {
        return sender;
    }


    public List<User> getRecipients() {
        return recipients;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(User user) {
        this.sender = user;
    }


    public void setRecipients(List<User> recipients) {
        this.recipients = recipients;
    }
}
