package se.sundsvall.incident.integration.lifebuoy;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import se.sundsvall.incident.integration.AbstractIntegrationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "integration.lifebuoy")
class LifeBuoyProperties extends AbstractIntegrationProperties {
    private String apikey;
}
