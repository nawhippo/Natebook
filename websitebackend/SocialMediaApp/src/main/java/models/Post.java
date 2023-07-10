package models;

import jakarta.persistence.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;


    //title
    String title;
    //description
    String description;
    //like/dislike
    int likes;
    int dislikes;




    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }



}
