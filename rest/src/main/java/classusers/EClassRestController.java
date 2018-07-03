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
//import com.fasterxml.jackson.databind.node.TextNode;


/**
 * @author Josh Long
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

	//************ not lastname?? or use id?? *************//
	@JsonView(View.General.class)
	@GetMapping("/user/all")
	Collection<User> readUserAll() {
		return this.userRepository.findAll();
	}

	@JsonView(View.General.class)
	@GetMapping("/user/{userId}/creator")
	Collection<EClass> readCreatorEClass(@PathVariable String userId) {
		this.validateUser(userId);

		return this.eclassRepository.findByCreatorLastname(userId);
	}

	@JsonView(View.Student.class)
	@GetMapping("/user/{userId}/student")
	Collection<EClass> readStudentEClass(@PathVariable String userId) {
		this.validateUser(userId);

		//return this.userRepository.findStudiedclassesByLastname(userId).getStudiedclasses();
		return this.userRepository.findByLastname(userId).get().getStudiedclasses();
	}

	@JsonView(View.Student.class)
	@GetMapping("/class/{classname}/students")
	Collection<User> readEClassStudent(@PathVariable String classname) {
		this.validateEClass(classname);

		return this.eclassRepository.findByClassname(classname).get().getStudents();
	}

	@PostMapping("/class/{classId}/rename")
	ResponseEntity<?> renameClass(@PathVariable Long classId, @RequestBody EClass input) {
		this.validateEClassId(classId);

		return this.eclassRepository
				.findById(classId)
				.map(eclass -> {
					eclass.setClassname(input.getClassname());
					EClass result = this.eclassRepository.save(eclass);

					// URI location = ServletUriComponentsBuilder
					// 	.fromCurrentRequest()
					// 	.path("/{id}")
					// 	.buildAndExpand(result.getId())
					// 	.toUri();

					return ResponseEntity.ok().build();
				})
				.orElse(ResponseEntity.noContent().build());
	}

	@PostMapping("/class/{classId}/addstudent")
	ResponseEntity<?> renameClass(@PathVariable Long classId, @RequestBody User input) {
		this.validateEClassId(classId);
		this.validateUserId(input.getId());
		
		return this.eclassRepository
				.findById(classId)
				.map(eclass -> {
					User student = this.userRepository.findById(input.getId()).get();
					//EClass updatedclass = eclass;
					Set<User> newstudents = eclass.getStudents();
					Set<EClass> newclasses = student.getStudiedclasses();
					newstudents.add(student);
					newclasses.add(eclass);
					eclass.setStudents(newstudents);
					student.setStudiedclasses(newclasses);
					//student.getStudiedclasses().add(updatedclass);
					this.eclassRepository.save(eclass);
					this.userRepository.save(student);

					// User student = this.userRepository.findById(input.getId()).get();
					// this.userRepository.save(student);
					// eclass.getStudents().add(student);
					// student.getStudiedclasses().add(eclass);
					// //EClass result = this.eclassRepository.save(eclass);
					// //this.userRepository.save(student);
					// this.eclassRepository.save(eclass);

					// URI location = ServletUriComponentsBuilder
					// 	.fromCurrentRequest()
					// 	.path("/{id}")
					// 	.buildAndExpand(result.getId())
					// 	.toUri();

					return ResponseEntity.ok().build();
				})
				.orElse(ResponseEntity.noContent().build());
	}

	@PostMapping("/user/{userId}/update")
	ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User input) {
		this.validateUserId(userId);

		return this.userRepository
				.findById(userId)
				.map(user -> {
					user.setFirstname(input.getFirstname());
					user.setLastname(input.getLastname());
					user.setEmail(input.getEmail());
					User result = this.userRepository.save(user);

					// URI location = ServletUriComponentsBuilder
					// 	.fromCurrentRequest()
					// 	.path("/{id}")
					// 	.buildAndExpand(result.getId())
					// 	.toUri();

					return ResponseEntity.ok().build();
				})
				.orElse(ResponseEntity.noContent().build());
	}

	// @PostMapping
	// ResponseEntity<?> add(@PathVariable String userId, @RequestBody Bookmark input) {
	// 	this.validateUser(userId);

	// 	return this.userRepository
	// 			.findByLastname(userId)
	// 			.map(user -> {
	// 				Bookmark result = this.bookmarkRepository.save(new Bookmark(user,
	// 						input.getUri(), input.getDescription()));

	// 				URI location = ServletUriComponentsBuilder
	// 					.fromCurrentRequest()
	// 					.path("/{id}")
	// 					.buildAndExpand(result.getId())
	// 					.toUri();

	// 				return ResponseEntity.created(location).build();
	// 			})
	// 			.orElse(ResponseEntity.noContent().build());
	// }

	@PutMapping("user/newuser")
	ResponseEntity<?> addUser(@RequestBody User input) {
		User newuser = new User(input.getFirstname(), input.getLastname(),input.getEmail());
		this.userRepository.save(newuser);
		return ResponseEntity.ok().build();
	}

	/**
	 * Verify the {@literal userId} exists.
	 *
	 * @param userId
	 */
	private void validateUser(String userId) {
		this.userRepository
			.findByLastname(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));
	}

	private void validateEClass(String classname) {
		this.eclassRepository
			.findByClassname(classname)
			.orElseThrow(() -> new EClassNotFoundException(classname));
	}

	private void validateEClassId(Long classId) {
		this.eclassRepository
			.findById(classId)
			.orElseThrow(() -> new EClassIdNotFoundException(classId));
	}

	private void validateUserId(Long userId) {
		this.userRepository
			.findById(userId)
			.orElseThrow(() -> new UserIdNotFoundException(userId));
	}
}
// end::code[]
