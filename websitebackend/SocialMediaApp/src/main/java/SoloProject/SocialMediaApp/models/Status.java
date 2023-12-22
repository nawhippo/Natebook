package SoloProject.SocialMediaApp.models;

import jakarta.persistence.*;
import org.joda.time.DateTime;

@Entity
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    String content;

    @Column
    private DateTime start;

    @Column
    int lifespan;

    DateTime death;


    Status() {

    }

    public Status(Long id, String content, DateTime start, int lifespan, int death) {
        this.id = id;
        this.content = content;
        this.start = start;
        this.lifespan = lifespan;
        this.death = start.plus((long)lifespan);
    }
}
