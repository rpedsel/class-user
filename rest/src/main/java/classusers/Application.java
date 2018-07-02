// tag::runner[]
package classusers;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository,
						   BookmarkRepository bookmarkRepository) {
		return args ->
			Arrays.asList("jhoeller","dsyer","pwebb","ogierke","rwinch","mfisher","mpollack","jlong")
				.forEach(lastname -> {
					User user = userRepository.save(new User("Tom", lastname, "email@example.com"));
					bookmarkRepository.save(new Bookmark(user, "http://bookmark.com/1/" + lastname, "A description"));
					bookmarkRepository.save(new Bookmark(user, "http://bookmark.com/2/" + lastname, "A description"));
				});
	}

}
// end::runner[]

