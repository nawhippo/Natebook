package SoloProject.SocialMediaApp.service;

import SoloProject.SocialMediaApp.models.CompressedImage;
import SoloProject.SocialMediaApp.repository.CompressedImageRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CompressionService {


    @Autowired
    private CompressedImageRepository compressedImageRepository;

    public CompressedImage compressImage(MultipartFile file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(800, 800)
                .outputQuality(0.75)
                .toOutputStream(outputStream);

        CompressedImage compressedImage = new CompressedImage();
        compressedImage.setWidth(800);
        compressedImage.setHeight(800);
        compressedImage.setFormat("JPEG");

        return compressedImageRepository.save(compressedImage);
    }
}