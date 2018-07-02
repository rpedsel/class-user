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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	@GetMapping("/user/{userId}/creator")
	Collection<EClass> readEClasses(@PathVariable String userId) {
		this.validateUser(userId);

		return this.eclassRepository.findByCreatorLastname(userId);
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

	// @GetMapping("/{bookmarkId}")
	// Bookmark readBookmark(@PathVariable String userId, @PathVariable Long bookmarkId) {
	// 	this.validateUser(userId);
		
	// 	return this.bookmarkRepository
	// 		.findById(bookmarkId)
	// 		.orElseThrow(() -> new BookmarkNotFoundException(bookmarkId));
	// }

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
}
// end::code[]
