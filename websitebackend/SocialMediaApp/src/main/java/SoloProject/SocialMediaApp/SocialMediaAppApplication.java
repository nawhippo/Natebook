package SoloProject.SocialMediaApp;

import org.apache.catalina.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
@SpringBootApplication
public class SocialMediaAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(SocialMediaAppApplication.class, args);

	}
}