package SoloProject.SocialMediaApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EntityScan(basePackages = "SoloProject.SocialMediaApp.models")
public class SocialMediaAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(SocialMediaAppApplication.class, args);

	}
}