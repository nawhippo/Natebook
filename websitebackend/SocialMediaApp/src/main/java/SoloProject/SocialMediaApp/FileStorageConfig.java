package SoloProject.SocialMediaApp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {


    @Bean
    public Path rootLocation() {
        Path storagePath = Paths.get("/storage");
        if (!Files.exists(storagePath)) {
            try {
                Files.createDirectories(storagePath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create storage directory", e);
            }
        }
        return storagePath;
    }
}