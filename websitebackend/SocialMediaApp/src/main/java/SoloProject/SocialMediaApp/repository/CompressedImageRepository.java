package SoloProject.SocialMediaApp.repository;

import SoloProject.SocialMediaApp.models.CompressedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CompressedImageRepository extends JpaRepository<CompressedImage, Long> {
    List<CompressedImage> findByPostid(Long postId);
    CompressedImage findByProfileid(Long postId);
}
