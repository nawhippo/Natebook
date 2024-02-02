package SoloProject.SocialMediaApp.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostCreationResponse {
    private Post post;
    private List<String> approvedUsernames;

    public PostCreationResponse(Post post, List<String> approvedUsernames) {
        this.post = post;
        this.approvedUsernames = approvedUsernames;
    }

}