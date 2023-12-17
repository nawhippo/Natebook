package SoloProject.SocialMediaApp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class CompressedImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long postid;

    @Column
    private Long profileId;

    @Lob
    private byte[] imageData; // Image data stored as a byte array

    @Column
    private int width;

    @Column
    private int height;

    @Column
    private String format;

    @Column(columnDefinition = "TEXT") // Use TEXT for long strings like Base64
    private String base64EncodedImage;
    public CompressedImage() {
        // Default constructor
    }

    public CompressedImage(byte[] imageData, int width, int height, String format, String base64EncodedImage,  Long profileId) {
        this.imageData = imageData;
        this.width = width;
        this.height = height;
        this.format = format;
        this.base64EncodedImage = base64EncodedImage;
        this.profileId = profileId;
    }

    public String getBase64EncodedImage() {
        return base64EncodedImage;
    }

    public void setBase64EncodedImage(String base64EncodedImage) {
        this.base64EncodedImage = base64EncodedImage;
    }
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostid() {
        return postid;
    }

    public void setPostid(Long postid) {
        this.postid = postid;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}