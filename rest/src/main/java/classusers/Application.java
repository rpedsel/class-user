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
			// init five users into repository, each creates two classes and joins the classes he/she creates 
			Arrays.asList("Amy Armstrong Art","Billy Brown Basketball","Chunya Chang Cooking","David Dawson Dancing","Erika Eto Ecommerce")
				.forEach(name -> {
					String[] splited = name.split(" ");
					String firstname = splited[0];
					String lastname = splited[1];
					String classname = splited[2];
					String email = firstname.toLowerCase()+lastname.substring(0,3).toLowerCase()+"@example.com";
					
					User creator = new User(firstname, lastname, email);
					userRepository.save(creator);
					
					EClass newclass = new EClass(creator, classname+" I");
					newclass.getStudents().add(creator);
					eclassRepository.save(newclass);

					EClass newclass2 = new EClass(creator, classname+" II");
					newclass2.getStudents().add(creator);
					eclassRepository.save(newclass2);

				});
	}

}
// end::runner[]

