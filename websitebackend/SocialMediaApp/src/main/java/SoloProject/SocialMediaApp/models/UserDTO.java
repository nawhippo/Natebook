package SoloProject.SocialMediaApp.models;

import java.util.List;

//DTO TO REMOVE RECUSRION LOOP FROM APPUSER
public class UserDTO {
    public UserDTO(Long id, String username, String firstname, String lastname, List<Post> posts, List<Message> messages) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.posts = posts;
        this.messages = messages;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    private Long id;
    private String username;
    private String firstname;
    private String lastname;

    List<Post> posts;

    List<Message> messages;
}
