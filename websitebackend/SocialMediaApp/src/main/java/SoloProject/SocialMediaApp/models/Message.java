package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //appuserid
    Long senderid;


    @CreationTimestamp
    private LocalDateTime timestamp;

    public void setId(Long id) {
        this.id = id;
    }

    public void setSenderid(Long senderid) {
        this.senderid = senderid;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setThreadid(Long threadid) {
        this.threadid = threadid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //threadid;
    Long threadid;

    String content;


    public Message(Long senderid, Long threadid, String content) {
        this.senderid = senderid;
        this.threadid = threadid;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public Long getSenderid() {
        return senderid;
    }

    public Long getThreadid() {
        return threadid;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Message{id=" + this.id + ", content='" + this.content + "', sender='" + this.senderid + "', ...other fields...}";
    }

    public Message(){

    }
}