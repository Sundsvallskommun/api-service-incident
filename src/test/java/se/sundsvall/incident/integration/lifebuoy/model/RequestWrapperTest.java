package se.sundsvall.incident.integration.lifebuoy.model;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

class RequestWrapperTest {

	@Test
	void testBuilderAndSerialization() {

		var mapper = new ObjectMapper();
		LifeBuoyRequestWrapper wrapper = LifeBuoyRequestWrapper.builder()
			.withApiKey("someApiKey")
			.withErrandJsonString(mapper.writeValueAsString(LifebuoyRequest.builder().build()))
			.build();

		String serialized;

		serialized = new ObjectMapper().writeValueAsString(wrapper);

		assertThat(serialized).isNotBlank();
	}
}
