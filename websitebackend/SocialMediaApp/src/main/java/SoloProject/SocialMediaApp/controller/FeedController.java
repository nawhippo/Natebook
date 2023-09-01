package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.Post;
import SoloProject.SocialMediaApp.models.PublicFeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FeedController {
    private final PublicFeed publicFeed;

    @Autowired
    public FeedController(PublicFeed publicFeed) {
        this.publicFeed = publicFeed;
    }

    @GetMapping("/public-feed")
    public ResponseEntity<List<Post>> getPublicFeed() {
        List<Post> feedData = publicFeed.getFeed();
        return ResponseEntity.ok(feedData);
    }
}