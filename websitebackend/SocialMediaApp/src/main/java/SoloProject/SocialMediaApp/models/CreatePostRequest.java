package SoloProject.SocialMediaApp.models;

        import java.util.List;

public class CreatePostRequest {
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    private Post post;
    private List<String> images;
}
