package se.sundsvall.incident.integration.lifebuoy;

import org.springframework.stereotype.Component;
import se.sundsvall.incident.integration.db.entity.IncidentEntity;

@Component
public class LifeBuoyIntegration {

	private final LifeBuoyClient client;
	private final LifeBuoyMapper mapper;

	public LifeBuoyIntegration(LifeBuoyClient client, LifeBuoyMapper mapper) {
		this.client = client;
		this.mapper = mapper;
	}

	public String sendLifeBuoy(IncidentEntity incident) {
		return client.sendLifebuoy(mapper.toLifeBuoyRequest(incident)).getBody();
	}
}
