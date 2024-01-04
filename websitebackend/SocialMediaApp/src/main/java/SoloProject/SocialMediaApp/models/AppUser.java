package SoloProject.SocialMediaApp.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "app_users")
public class AppUser implements UserDetails {

    public Long profilePicture;

    private int friendCount = 0;
    public Long getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Long profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isOnline;

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Boolean isGoogleUser;

    public void setGoogleUser(Boolean googleUser) {
        isGoogleUser = googleUser;
    }

    public Boolean getGoogleUser() {
        return isGoogleUser;
    }


    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
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
        this.isGoogleUser = false;
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
        this.role = "USER";
        this.isGoogleUser = false;
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