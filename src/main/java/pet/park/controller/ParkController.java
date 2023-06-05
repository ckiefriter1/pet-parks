package pet.park.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	private final AtomicLong counter = new AtomicLong();

	// REST API end-point for /greeting resource
	@GetMapping("/greeting")
	public Greeting greeting() {
		return new Greeting(counter.incrementAndGet(), "Hello World");
	}

	@Autowired
	private ParkService parkService;
	
	@PostMapping("/contributor")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ContributorData insertContributor(@RequestBody ContributorData contributorData) {
		log.info("Creating contributor {}", contributorData);
		return parkService.saveContributor(contributorData);
	}
}
