package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.CompressedImage;
import SoloProject.SocialMediaApp.repository.CompressedImageRepository;
import SoloProject.SocialMediaApp.service.CompressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    @Autowired
    private CompressionService compressionService;
    @Autowired
    private CompressedImageRepository compressedImageRepository;

    @PostMapping("/upload")
    public ResponseEntity<List<CompressedImage>> UploadImages(List<MultipartFile> photos) throws IOException {
        ArrayList<CompressedImage> returnPhotos = new ArrayList<>();
    for(MultipartFile photo : photos){
        CompressedImage image = compressionService.compressImage(photo);
        returnPhotos.add(image);
        }
    return ResponseEntity.ok(returnPhotos);
    }
}
