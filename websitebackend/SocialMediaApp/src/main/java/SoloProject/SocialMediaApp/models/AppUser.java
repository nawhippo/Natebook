package SoloProject.SocialMediaApp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "app_users")
public class AppUser implements UserDetails {

    public AppUser(String firstName, String lastName, String email, String encodedPassword, String username) {
    this.firstname = firstName;
    this.lastname = lastName;
    this.email = email;
    this.password = encodedPassword;
    this.username = username;
    this.following = new ArrayList<>();
    this.followers = new ArrayList<>();
    this.friends = new ArrayList<>();
    this.requests = new ArrayList<>();
    this.blockList = new ArrayList<>();
    }


    private List<Long> followers;
    private List<Long> following;
    public Long profilePicture;

    private int friendCount = 0;

    @Column
    private String profileColor;


    public void setProfileColor(String profileColor) {
        this.profileColor = profileColor;
    }


    public void setProfilePicture(Long profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isOnline;

    @Column
    public Boolean isPrivate = false;


    private List<Long> requests;

    private List<Long> blockList;

    public AppUser(String firstName, String lastName, String email, String password, String username, String profileColor) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email=  email;
        this.password = password;
        this.username = username;
        this.blockList = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.isPrivate = false;
        this.profileColor = profileColor;
    }

    public void setRequests(List<Long> requests) {
        this.requests = requests;
    }


    private String role;

    public void setRole(String role) {
        this.role = role;
    }

    public void setAppUserID(Long appUserID) {
        this.appUserID = appUserID;
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
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
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
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
    }

    public AppUser() {
        this.friends = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.role = "USER";
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority")
    private Set<String> authorities = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

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