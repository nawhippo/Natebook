package SoloProject.SocialMediaApp.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStatusRequest {
    private String content;
    private int lifespan;
}
