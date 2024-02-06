package SoloProject.SocialMediaApp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;
import java.time.LocalDateTime;


@NoArgsConstructor
@Entity
@AllArgsConstructor
@Getter
@Setter
public class EmailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String token;
    @Column
    private String email;
    @Column
    private LocalDateTime expiryDate;

    @Column
    private boolean verified;
}