package SoloProject.SocialMediaApp.models;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class AppUserDTO {

    public AppUserDTO() {
    }

    private Long appUserID;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private List<Long> friends;
    private List<Long> requests;
    private List<Long> blockList;
    private boolean isOnline;
    private Boolean isGoogleUser;
    private int friendCount;
    private String role;
    private Long profilePicture;

    public Long getAppUserID() {
        return appUserID;
    }

    public void setAppUserID(Long appUserID) {
        this.appUserID = appUserID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
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

    public List<Long> getRequests() {
        return requests;
    }

    public void setRequests(List<Long> requests) {
        this.requests = requests;
    }

    public List<Long> getBlockList() {
        return blockList;
    }

    public void setBlockList(List<Long> blockList) {
        this.blockList = blockList;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Boolean getGoogleUser() {
        return isGoogleUser;
    }

    public void setGoogleUser(Boolean googleUser) {
        isGoogleUser = googleUser;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Long profilePicture) {
        this.profilePicture = profilePicture;
    }

    public AppUserDTO(AppUser appUser) {
        this.appUserID = appUser.getAppUserID();
        this.firstname = appUser.getFirstname();
        this.lastname = appUser.getLastname();
        this.email = appUser.getEmail();
        this.username = appUser.getUsername();
        this.blockList = appUser.getBlockList();
        this.isOnline = appUser.isOnline();
        this.friendCount = appUser.getFriendCount();
        this.role = appUser.getRole();
        this.profilePicture = appUser.getProfilePicture();
    }
}