package se.sundsvall.incident.integration.lifebuoy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.sundsvall.incident.TestDataFactory.createIncidentEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import se.sundsvall.incident.integration.lifebuoy.model.LifeBuoyRequestWrapper;

@ExtendWith(MockitoExtension.class)
class LifeBuoyIntegrationTest {


	@Mock
	private LifeBuoyClient mockClient;

	@Mock
	private LifeBuoyMapper mockMapper;

	@InjectMocks
	private LifeBuoyIntegration lifeBuoyIntegration;

	@Test
	void sendLifeBuoyTest() throws JsonProcessingException {
		var incident = createIncidentEntity();
		when(mockMapper.toLifeBuoyRequest(any())).thenReturn(LifeBuoyRequestWrapper.builder().build());
		when(mockClient.sendLifebuoy(any())).thenReturn(new ResponseEntity<String>(HttpStatus.OK));

		lifeBuoyIntegration.sendLifeBuoy(incident);

		verify(mockMapper).toLifeBuoyRequest(incident);
		verify(mockClient).sendLifebuoy(any());
	}
}
