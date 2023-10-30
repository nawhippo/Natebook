package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.CompressedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompressedImageRepository extends JpaRepository<CompressedImage, Long> {

}
