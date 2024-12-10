package se.sundsvall.incident.integration.messaging;

import generated.se.sundsvall.messaging.MessageResult;
import java.util.Optional;
import org.springframework.stereotype.Component;
import se.sundsvall.incident.integration.db.entity.IncidentEntity;

@Component
public class MessagingIntegration {

	private final MessagingMapper messagingMapper;
	private final MessagingClient client;

	public MessagingIntegration(final MessagingMapper messagingMapper, final MessagingClient client) {
		this.messagingMapper = messagingMapper;
		this.client = client;
	}

	public Optional<MessageResult> sendEmail(final IncidentEntity incident) {
		return Optional.of(client.sendEmail(incident.getMunicipalityId(), messagingMapper.toEmailDto(incident)));
	}

	public Optional<MessageResult> sendMSVAEmail(final IncidentEntity incident) {
		return Optional.of(client.sendEmail(incident.getMunicipalityId(), messagingMapper.toMSVAEmailRequest(incident)));
	}
}
