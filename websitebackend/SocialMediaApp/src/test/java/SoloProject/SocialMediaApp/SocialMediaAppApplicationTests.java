package SoloProject.SocialMediaApp;

import models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import repository.UserRepository;

import java.util.Collections;
import java.util.List;

@ComponentScan(basePackages = "repository")

@SpringBootTest
class SocialMediaAppApplicationTests {
	private UserRepository userRepository;


	private TestEntityManager entityManager;

	@Test
	void contextLoads() {
		User test = new User();
		test.setFirstname("Larry");
		test.setLastname("Fart");
		test.setEmail("NateWip@Cooledudes.com");
		test.setUsername("LarryLoffa");
		test.setPosts(Collections.emptyList());
		test.setMessages(Collections.emptyList());
		test.setFriends(Collections.emptyList());
		User saveduser = userRepository.save(test);


		User retrievedUser = entityManager.find(User.class, saveduser);

	}

}
