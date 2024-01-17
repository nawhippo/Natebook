package SoloProject.SocialMediaApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;


@SpringBootApplication
@EntityScan(basePackages = "SoloProject.SocialMediaApp.models")
@CrossOrigin(origins = "http://localhost:3000")
public class SocialMediaAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(SocialMediaAppApplication.class, args);

	}
}