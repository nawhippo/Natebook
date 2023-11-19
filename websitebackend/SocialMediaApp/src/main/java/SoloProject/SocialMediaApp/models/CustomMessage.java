package SoloProject.SocialMediaApp.models;

import java.util.ArrayList;

public class CustomMessage {
    public long getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(long chatGroupId) {
        this.chatGroupId = chatGroupId;
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

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public long getOriginalMessageId() {
        return originalMessageId;
    }

    public void setOriginalMessageId(long originalMessageId) {
        this.originalMessageId = originalMessageId;
    }

    private String title;

    private String body;
    private long sender;
    private long chatGroupId;
    private long originalMessageId;

    public CustomMessage(String title, String body, long sender, long chatGroupId, long originalMessageId) {
        this.title = title;
        this.body = body;
        this.sender = sender;
        this.chatGroupId = chatGroupId;
        this.originalMessageId = originalMessageId;
    }
}
