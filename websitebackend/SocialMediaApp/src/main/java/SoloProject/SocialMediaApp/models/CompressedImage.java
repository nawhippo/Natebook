package SoloProject.SocialMediaApp.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "images")
public class CompressedImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long postid;

    @Column
    private Long profileid;

    public void setProfileid(Long profileid) {
        this.profileid = profileid;
    }

    @Lob
    private byte[] imageData;

    @Column
    private int width;

    @Column
    private int height;

    @Column
    private String format;


    //long string, so text is necessary
    @Column(columnDefinition = "TEXT")
    private String base64EncodedImage;


    public CompressedImage(byte[] imageData, int width, int height, String format, String base64EncodedImage) {
        this.imageData = imageData;
        this.width = width;
        this.height = height;
        this.format = format;
        this.base64EncodedImage = base64EncodedImage;
    }

}