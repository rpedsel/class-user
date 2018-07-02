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
						   EClassRepository eclassRepository) {
		return args ->
			Arrays.asList("jhoeller","dsyer","pwebb","ogierke","rwinch","mfisher","mpollack","jlong")
				.forEach(lastname -> {
					User creator = userRepository.save(new User("Tom", lastname, "email@example.com"));
					eclassRepository.save(new EClass(creator, "A Class by "+lastname));
					eclassRepository.save(new EClass(creator, "B Class by "+lastname));
				});
	}

}
// end::runner[]

