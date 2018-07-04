// tag::runner[]
package classusers;

import java.util.Collection;
import java.util.Arrays;
import java.util.Optional;
import static java.lang.System.out;

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
					User creator = new User("Tom", lastname, "email@example.com");
					userRepository.save(creator);
					EClass newclass = new EClass(creator, "A Class by "+lastname);
					newclass.getStudents().add(creator);

					eclassRepository.save(newclass);

					EClass newclass2 = new EClass(creator, "B Class by "+lastname);
					newclass2.getStudents().add(creator);

					eclassRepository.save(newclass2);

					// Collection<User> testclass = userRepository.findStudiedclassesByLastname(lastname);
					// out.println("********************************************************");
					// out.println(testclass.iterator().next());
					// out.println("********************************************************");
					// Optional<EClass> testclass = eclassRepository.findByClassname(newclass.getClassname());
					// out.println("********************************************************");
					// out.println(newclass.getStudents().iterator().next().getLastname());
					// out.println("********************************************************");
					// out.println(testclass.get().getStudents().isEmpty());
				});
	}

}
// end::runner[]

