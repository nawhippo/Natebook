package SoloProject.SocialMediaApp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
}