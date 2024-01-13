package SoloProject.SocialMediaApp.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "app_users")
public class AppUser implements UserDetails {

    private LocalDateTime lastChecked;

    public LocalDateTime getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(LocalDateTime lastChecked) {
        this.lastChecked = lastChecked;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public Long profilePicture;

    private int friendCount = 0;

    private int messageCount = 0;
    public Long getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Long profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isOnline;

    @Column
    public Boolean isPrivate = false;

    public Boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }







    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }


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
        this.isPrivate = false;
        this.messageCount = 0;
    }

    public List<Long> getRequests() {
        return requests;
    }

    public void setRequests(List<Long> requests) {
        this.requests = requests;
    }


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
    private String occupation;

    @Column
    private String biography;
    @Column
    private String email;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column
    private Boolean verified;

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }



    @ElementCollection
    @CollectionTable(name = "user_friends", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "friend_id")
    private List<Long> friends;


    public AppUser(String firstname, String lastname, String email, String username, List<Long> friends, String password, List<Long> requests) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.friends = friends;
        this.password = password;
        this.requests = requests;
        this.verified = false;
    }

    public AppUser(String firstname, String lastname, String email, String username, List<Long> friends, String password, List<Long> requests, String occupation, String biography) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.friends = friends;
        this.password = password;
        this.requests = requests;
        this.verified = false;
        this.occupation = occupation;
        this.biography = biography;
        this.messageCount = 0;
    }

    public AppUser() {
        this.friends = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.role = "USER";
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority")
    private Set<String> authorities = new HashSet<>();

    public void setRoles(String[] roles) {
        this.authorities.clear();
        for (String role : roles) {
            this.authorities.add(role);
        }
    }

    public void addAuthority(String authority) {
        this.authorities.add(authority);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (String authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        return grantedAuthorities;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Long getId() {
        return this.appUserID;
    }


}