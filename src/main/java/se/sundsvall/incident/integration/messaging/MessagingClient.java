package se.sundsvall.incident.integration.messaging;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static se.sundsvall.incident.integration.messaging.configuration.MessagingConfiguration.REGISTRATION_ID;

import generated.se.sundsvall.messaging.EmailRequest;
import generated.se.sundsvall.messaging.MessageResult;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se.sundsvall.incident.integration.messaging.configuration.MessagingConfiguration;

@FeignClient(
	name = REGISTRATION_ID,
	url = "${integration.messaging.url}",
	configuration = MessagingConfiguration.class)
@CircuitBreaker(name = REGISTRATION_ID)
public interface MessagingClient {

	/**
	 * Send a single e-mail
	 *
	 * @param  municipalityId id of municipality
	 * @param  request        containing email information
	 * @return                response containing id and delivery results for sent message
	 */
	@PostMapping(path = "/{municipalityId}/email", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	MessageResult sendEmail(@PathVariable final String municipalityId, @RequestBody final EmailRequest request);
}
