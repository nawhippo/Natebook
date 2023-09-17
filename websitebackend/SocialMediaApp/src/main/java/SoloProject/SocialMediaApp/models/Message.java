package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    public Message(String content, AppUser sender, List<String> recipients) {
        this.content = content;
        this.sender = sender;
        this.isIncoming = isIncoming;
        this.recipients = recipients;
        this.dateTime = new Date();
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
    private int senderid;

    public int getSenderid() {
        return senderid;
    }

    public void setSenderid(int senderid) {
        this.senderid = senderid;
    }

    @Column
    private String content;

    @Column
    private String title;

    @Column
    private String senderusername;

    public String getSenderusername() {
        return senderusername;
    }

    public void setSenderusername(String senderusername) {
        this.senderusername = senderusername;
    }

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

    //Could potentially be itself.
    @ManyToOne
    @JsonBackReference(value = "parent")
    @JoinColumn(name = "parent_message_id")
    private Message parentMessage;

    @OneToMany(mappedBy = "parentMessage", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "parent")
    private List<Message> childMessages = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Message getParentMessage() {
        return parentMessage;
    }

    public void setParentMessage(Message parentMessage) {
        this.parentMessage = parentMessage;
    }

    public List<Message> getChildMessages() {
        return childMessages;
    }

    public void setChildMessages(List<Message> childMessages) {
        this.childMessages = childMessages;
    }

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