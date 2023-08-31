package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.Post;

import java.util.List;

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


    void removeFromFeed(Post post){
        this.getFeed().remove(post);
    }

    //sorting functionality?


}
