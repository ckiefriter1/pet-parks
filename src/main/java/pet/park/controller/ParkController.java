package pet.park.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.park.controller.model.ContributorData;
import pet.park.controller.model.Greeting;
import pet.park.service.ParkService;

@RestController
@RequestMapping("/pet_park")
@Slf4j
public class ParkController {
	
	@Autowired
	private ParkService parkService;
	
	private final AtomicLong counter = new AtomicLong();

	// REST API end-point for /greeting resource
	@GetMapping("/greeting")
	public Greeting greeting() {
		return new Greeting(counter.incrementAndGet(), "Hello World");
	}

	@GetMapping("/contributor")
	public List<ContributorData> retrieveAllContributors() {
		log.info("Retrieve all contributors called.");
		return parkService.retrieveAllContributors();
	}

	
	@GetMapping("/contributor/{contributorId}")
	public ContributorData retrieveContributorById(@PathVariable Long contributorId) {
		log.info("Retrieve a contributor for ID = {}", contributorId);
		return parkService.retrieveContributorById(contributorId);
	}

	/*
	 * POST method to create new contributors.
	 */
	@PostMapping("/contributor")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ContributorData insertContributor(@RequestBody ContributorData contributorData) {
		log.info("Creating contributor {}", contributorData);
		return parkService.saveContributor(contributorData);
	}

	/*
	 * PUT method to update contributors
	 */
	@PutMapping("/contributor/{contributorId}")
	public ContributorData updateContributor(@PathVariable Long contributorId, @RequestBody ContributorData contributorData) {
		contributorData.setContributorId(contributorId);
		log.info("Updating contributor ID = {}", contributorId);
		return parkService.saveContributor(contributorData);
	}

	/*
	 * DELETE method to create new contributors.
	 */
	@DeleteMapping("/contributor")
	public void deleteAllContributors() {
		log.info("Attempting to delete all contributors.");
		throw new UnsupportedOperationException("Deleting all contributors is not allowed.");
	}

	/*
	 * DELETE method to delete a specific contributor by ID.
	 */
	@DeleteMapping("/contributor/{contributorId}")
	public Map<String, String> deleteContributorById(@PathVariable Long contributorId) {
		log.info("Deleting contributor for ID = {}", contributorId);
		parkService.deleteContributorById(contributorId);
		return Map.of("message", "Deletion of contributor with ID = " + contributorId + " was successful.");
	}


}
