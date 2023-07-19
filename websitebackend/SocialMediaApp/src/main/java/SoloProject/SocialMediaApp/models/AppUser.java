package SoloProject.SocialMediaApp.models;
import jakarta.persistence.*;
import jakarta.persistence.Id;

import java.util.Collections;
import java.util.List;
//is a database entity, how fun.




@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public AppUser(String firstname, String lastname, String email, String username, List<Post> posts, List<Message> messages, List<AppUser> friends) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.posts = posts;
        this.messages = messages;
        this.friends = friends;
    }


    public AppUser(String firstname, String lastname, String email, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.posts = Collections.emptyList();
        this.messages = Collections.emptyList();
        this.friends = Collections.emptyList();
    }

    public AppUser() {

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

    public List<AppUser> getFriends() {
        return friends;
    }


    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String email;

    @Column
    private String username;

    @OneToMany(mappedBy = "app_user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> messages;

    @OneToMany
    @JoinColumn(name = "friend_id")
    private List<AppUser> friends;

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

    public void setFriends(List<AppUser> friends) {
        this.friends = friends;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}