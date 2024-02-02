package SoloProject.SocialMediaApp.models;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AppUserDTO {

    private boolean isPrivate;

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }


    private int MessageCount;
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

    @Column
    private String profileColor;

    private String occupation;
    private String biography;
    private String role;
    private Long profilePicture;



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
        this.isPrivate = appUser.getIsPrivate();
        this.profileColor = appUser.getProfileColor();
        this.occupation = appUser.getOccupation();
        this.biography = appUser.getBiography();
    }
}