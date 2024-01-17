package SoloProject.SocialMediaApp.models;

public class CreateStatusRequest {
    private String content;
    private int lifespan;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }
}
