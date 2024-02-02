package SoloProject.SocialMediaApp.models;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePostRequest {
    private Post post;
    private List<String> images;
}
