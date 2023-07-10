package SoloProject.SocialMediaApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories

@SpringBootApplication
public class SocialMediaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaAppApplication.class, args);
	}

}
