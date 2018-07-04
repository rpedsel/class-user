/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package classusers;

import java.net.URI;
import java.util.Collection;
import java.util.Set;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonView;


/**
 * author of spring boot REST tutorial (https://spring.io/guides/tutorials/bookmarks/)
 * @author Josh Long
 * 
 * @author I-Hui Huang 
 */
// tag::code[]

@RestController
@RequestMapping
class EClassRestController {

	private final EClassRepository eclassRepository;
	private final UserRepository userRepository;

	@Autowired
	EClassRestController(EClassRepository eclassRepository,
						   UserRepository userRepository) {
		this.eclassRepository = eclassRepository;
		this.userRepository = userRepository;
	}

	// get list of all users
	@JsonView(View.General.class)
	@GetMapping("/user/all")
	Collection<User> readUserAll() {
		return this.userRepository.findAll();
	}

	// get all classes that a user is a creator for
	@JsonView(View.General.class)
	@GetMapping("/user/{userId}/creator")
	Collection<EClass> readCreatorEClass(@PathVariable Long userId) {
		this.validateUser(userId);
		return this.eclassRepository.findByCreatorId(userId);
	}

	// get all classes that a user is a student for
	@JsonView(View.General.class)
	@GetMapping("/user/{userId}/student")
	Collection<EClass> readStudentEClass(@PathVariable Long userId) {
		this.validateUser(userId);
		return this.userRepository.findById(userId).get().getStudiedclasses();
	}

	// get all students user objects for a class (this should return a list of students)
	@JsonView(View.Student.class)
	@GetMapping("/class/{classId}/students")
	Collection<User> readEClassStudent(@PathVariable Long classId) {
		this.validateEClass(classId);

		return this.eclassRepository.findById(classId).get().getStudents();
	}

	// update a class name
	@PostMapping("/class/{classId}/rename")
	ResponseEntity<?> renameClass(@PathVariable Long classId, @RequestBody EClass input) {
		this.validateEClass(classId);

		// validate input class name: not null and not empty string ""
		boolean check = (input.getClassname() != null && !input.getClassname().isEmpty());
		Optional<String> opt = check ? Optional.of(input.getClassname()) : Optional.empty();
		this.validateInput(opt, "classname");

		return this.eclassRepository
				.findById(classId)
				.map(eclass -> {
					eclass.setClassname(input.getClassname());
					EClass result = this.eclassRepository.save(eclass);

					URI location = ServletUriComponentsBuilder
						.fromCurrentContextPath()
					 	.path("/class/{Id}")
						.buildAndExpand(result.getId())
						.toUri();

					return ResponseEntity.created(location).build();
				})
				.orElse(ResponseEntity.noContent().build());
	}

	// add a student to a class
	@PostMapping("/class/{classId}/addstudent")
	ResponseEntity<?> renameClass(@PathVariable Long classId, @RequestBody User input) {
		this.validateEClass(classId);
		this.validateUser(input.getId());
		
		return this.eclassRepository
				.findById(classId)
				.map(eclass -> {
					User student = this.userRepository.findById(input.getId()).get();
					Set<User> newstudents = eclass.getStudents();
					newstudents.add(student);
					eclass.setStudents(newstudents);
					
					EClass result = this.eclassRepository.save(eclass);

					URI location = ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/class/{Id}/students")
						.buildAndExpand(result.getId())
						.toUri();

					return ResponseEntity.created(location).build();
				})
				.orElse(ResponseEntity.noContent().build());
	}

	// update a student's first name, last name, and/or email
	@PostMapping("/user/{userId}/update")
	ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User input) {
		this.validateUser(userId);

		// validate input - one of three field exist and not empty string ""
		boolean checkFirstname = (input.getFirstname() != null && !input.getFirstname().isEmpty());
		boolean checkLastname = (input.getLastname() != null && !input.getLastname().isEmpty());
		boolean checkEmail = (input.getEmail() != null && !input.getEmail().isEmpty());
		Optional<User> opt = (!checkFirstname && !checkLastname && !checkEmail) ? Optional.empty() : Optional.of(input);
		this.validateInput(opt, "all of firstname, lastname and email");
	
		return this.userRepository
				.findById(userId)
				.map(user -> {
					if (checkFirstname)
						user.setFirstname(input.getFirstname());
					if (checkLastname)
						user.setLastname(input.getLastname());
					if (checkEmail)
						user.setEmail(input.getEmail());
					User result = this.userRepository.save(user);

					URI location = ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/user/{Id}")
						.buildAndExpand(result.getId())
						.toUri();

					return ResponseEntity.created(location).build();
				})
				.orElse(ResponseEntity.noContent().build());
	}

	// create a User
	@PutMapping("user/create")
	ResponseEntity<?> addUser(@RequestBody User input) {
		User newuser = new User(input.getFirstname(), input.getLastname(),input.getEmail());
		User result = this.userRepository.save(newuser);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath()
			.path("/user/{Id}")
			.buildAndExpand(result.getId())
			.toUri();

		return ResponseEntity.created(location).build();
	}

	@PutMapping("class/create/{userId}")
	ResponseEntity<?> addClass(@PathVariable Long userId, @RequestBody EClass input) {
		this.validateUser(userId);
		return this.userRepository
				.findById(userId)
				.map(user -> {
					EClass newclass = new EClass(user, input.getClassname());
					this.eclassRepository.save(newclass);
					// URI location = ServletUriComponentsBuilder
					// 	.fromCurrentRequest()
					// 	.path("/{id}")
					// 	.buildAndExpand(result.getId())
					// 	.toUri();

					return ResponseEntity.ok().build();
				})
				.orElse(ResponseEntity.noContent().build());
	}

	/**
	 * Verify the {@literal userId} exists.
	 *
	 * @param userId
	 */

	private void validateEClass(Long classId) {
		this.eclassRepository
			.findById(classId)
			.orElseThrow(() -> new EClassNotFoundException(classId));
	}

	private void validateUser(Long userId) {
		this.userRepository
			.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));
	}

	private void validateInput(Optional<?> value, String field) {
	   value.orElseThrow(() -> new InvalidInputException(field));
	}
}
// end::code[]
