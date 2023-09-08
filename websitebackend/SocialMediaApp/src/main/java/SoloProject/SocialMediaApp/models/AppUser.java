package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "app_users")
public class AppUser {

    @Column
    private String profilePictureUrl;

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @Column
    private List<Long> requests;

    private List<Long> blockList;

    public List<Long> getBlockList() {
        return blockList;
    }

    public void setBlockList(List<Long> blockList) {
        this.blockList = blockList;
    }

    public AppUser(String firstName, String lastName, String email, String password, String username) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email=  email;
        this.password = password;
        this.username = username;
        this.blockList = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.role = "USER";
    }

    public List<Long> getRequests() {
        return requests;
    }

    public void setRequests(List<Long> requests) {
        this.requests = requests;
    }

    @Column
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAppUserID(Long appUserID) {
        this.appUserID = appUserID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appUserID;


    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    @JsonManagedReference
    private List<Message> messages;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String email;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Post> posts;


    @ElementCollection
    @CollectionTable(name = "user_friends", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "friend_id")
    private List<Long> friends;


    public AppUser(String firstname, String lastname, String email, String username, List<Post> posts, List<Message> messages, List<Long> friends, String password, List<Long> requests, List<Message> messages1) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.posts = posts;
        this.friends = friends;
        this.password = password;
        this.requests = requests;
        this.messages = messages1;
        this.role = "USER";
    }

    public AppUser(List<Message> messages, String firstname, String lastname, String username, String email, String password) {
        this.messages = messages;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.posts = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.blockList = new ArrayList<>();
        this.role = "USER";
    }

    public AppUser() {
        this.messages = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.role = "USER";
    }

    public Long getAppUserID() {
        return appUserID;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }


    public List<Long> getFriends() {
        return friends;
    }

    public void setFriends(List<Long> friends) {
        this.friends = friends;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + appUserID +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}