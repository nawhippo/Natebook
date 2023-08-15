package SoloProject.SocialMediaApp.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "app_users")
public class AppUser {
    @ElementCollection
    @CollectionTable(name = "friend_requests", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "request")
    @JsonManagedReference
    private List<Long> requests;

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

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private String password;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Post> posts;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Message> messages;




    @ElementCollection
    @CollectionTable(name = "user_friends", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "friend_id")
    private List<Long> friends;


    public AppUser(String firstname, String lastname, String email, String username, List<Post> posts, List<Message> messages, List<Long> friends, String password, List<Long> requests) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.posts = posts;
        this.messages = messages;
        this.friends = friends;
        this.password = password;
        this.requests = requests;
    }

    public AppUser(String firstname, String lastname, String email, String username, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.posts = Collections.emptyList();
        this.messages = Collections.emptyList();
        this.friends = Collections.emptyList();
        this.requests = Collections.emptyList();
        this.role = "USER";
    }

    public AppUser() {
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Long> getFriends() {
        return friends;
    }

    public void setFriends(List<Long> friends) {
        this.friends = friends;
    }




    //has to be implemented in appuser service, we have to be able to access the repository
    public void sendFriendRequest(AppUser receiver){
    }

    public void acceptFriendRequest(AppUser receiver){

    }

    public void declineFriendRequest(AppUser receiver){

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