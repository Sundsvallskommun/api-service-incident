package se.sundsvall.incident.integration.lifebuoy;

import static se.sundsvall.incident.integration.lifebuoy.configuration.LifeBuoyConfiguration.REGISTRATION_ID;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se.sundsvall.incident.integration.lifebuoy.configuration.LifeBuoyConfiguration;
import se.sundsvall.incident.integration.lifebuoy.model.LifeBuoyRequestWrapper;

@FeignClient(
	name = REGISTRATION_ID,
	url = "${integration.lifebuoy.url}",
	configuration = LifeBuoyConfiguration.class)
@CircuitBreaker(name = REGISTRATION_ID)
interface LifeBuoyClient {

	@PostMapping(consumes = "application/x-www-form-urlencoded")
	ResponseEntity<String> sendLifebuoy(@RequestBody LifeBuoyRequestWrapper request);
}
