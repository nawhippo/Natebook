package SoloProject.SocialMediaApp.models;

import java.util.List;

public class PostCreationResponse {
    private Post post;
    private List<String> approvedUsernames;

    public PostCreationResponse(Post post, List<String> approvedUsernames) {
        this.post = post;
        this.approvedUsernames = approvedUsernames;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<String> getApprovedUsernames() {
        return approvedUsernames;
    }

    public void setApprovedUsernames(List<String> approvedUsernames) {
        this.approvedUsernames = approvedUsernames;
    }
}