package SoloProject.SocialMediaApp.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_user_id")
    private Long appUserID;

    @Column
    private String content;

    @Column(name = "start_time")
    private LocalDateTime start;

    @Column
    private int lifespan;

    @Column(name = "death_time")
    private LocalDateTime death;

    public Status() {
    }

    public Status(Long id, Long appUserID, String content, LocalDateTime start, int lifespan, LocalDateTime death) {
        this.id = id;
        this.appUserID = appUserID;
        this.content = content;
        this.start = start;
        this.lifespan = lifespan;
        this.death = death;
    }


    public Status(Long appUserId, String content, LocalDateTime start, int lifespan, LocalDateTime death) {
        this.appUserID = appUserId;
        this.content = content;
        this.start = start;
        this.lifespan = lifespan;
        this.death = death;
    }


    public Status(Long appUserId, String content, int lifespan) {
        this.appUserID = appUserId;
        this.content = content;
        this.start = LocalDateTime.now();
        this.lifespan = lifespan;
        this.death = this.start.plusHours(lifespan);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppUserID() {
        return appUserID;
    }

    public void setAppUserID(Long appUserID) {
        this.appUserID = appUserID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public LocalDateTime getDeath() {
        return death;
    }

    public void setDeath(LocalDateTime death) {
        this.death = death;
    }
}