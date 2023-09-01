package SoloProject.SocialMediaApp.models;

import SoloProject.SocialMediaApp.models.Post;
import org.springframework.stereotype.Component;

import java.util.List;



@Component
public class PublicFeed {
    List<Post> Feed;

    public PublicFeed(List<Post> feed) {
        Feed = feed;
    }

    public List<Post> getFeed() {
        return Feed;
    }

    public void setFeed(List<Post> feed) {
        Feed = feed;
    }

    void addToFeed(Post post){
        this.getFeed().add(post);
    }


    public void removeFromFeed(Post post){
        this.getFeed().remove(post);
    }

    //sorting functionality?


}
