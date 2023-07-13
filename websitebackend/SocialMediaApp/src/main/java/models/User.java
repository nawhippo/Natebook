package models;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.util.List;
//is a database entity, how fun.




@Entity
@Table(name = "users")
public class User {
    public User(Long id, String firstname, String lastname, String email, String username, List<Post> posts, List<Message> messages, List<User> friends) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.posts = posts;
        this.messages = messages;
        this.friends = friends;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<User> getFriends() {
        return friends;
    }

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String firstname;
    String lastname;
    String email;

    String username;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<User> friends;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}