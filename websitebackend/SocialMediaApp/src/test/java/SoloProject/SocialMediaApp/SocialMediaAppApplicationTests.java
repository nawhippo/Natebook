package SoloProject.SocialMediaApp;
import SoloProject.SocialMediaApp.repository.AppUserRepository;
import org.springframework.context.annotation.ComponentScan;
import SoloProject.SocialMediaApp.models.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Collections;

@SpringBootTest
@ComponentScan(basePackages = {"UserRepository"})
class SocialMediaAppApplicationTests {

@Autowired
private AppUserRepository appUserRepository;

@Autowired
private TestEntityManager entityManager;

	@Test
	void contextLoads() {
		AppUser test = new AppUser();
		test.setFirstname("Larry");
		test.setLastname("Fart");
		test.setEmail("NateWip@Cooledudes.com");
		test.setUsername("LarryLoffa");
		test.setPosts(Collections.emptyList());
		test.setMessages(Collections.emptyList());
		test.setFriends(Collections.emptyList());
		AppUser savedAppUser = appUserRepository.save(test);

		AppUser retrievedAppUser = entityManager.find(AppUser.class, savedAppUser.getAppUserID());
		// Add assertions or further test logic as needed
	}
}