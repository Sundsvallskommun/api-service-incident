package se.sundsvall.incident.integration.messaging;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.sundsvall.incident.TestDataFactory.MUNICIPALITY_ID;
import static se.sundsvall.incident.TestDataFactory.createEmailRequest;
import static se.sundsvall.incident.TestDataFactory.createIncidentEntity;
import static se.sundsvall.incident.TestDataFactory.createMSVAEmailRequest;

import generated.se.sundsvall.messaging.EmailRequest;
import generated.se.sundsvall.messaging.MessageResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessagingIntegrationTest {

	@Mock
	private MessagingMapper mockMessagingMapper;

	@Mock
	private MessagingClient mockMessagingClient;

	@InjectMocks
	private MessagingIntegration messagingIntegration;

	@Test
	void sendEmail() {
		var incident = createIncidentEntity();
		incident.setMunicipalityId(MUNICIPALITY_ID);
		when(mockMessagingMapper.toEmailDto(any())).thenReturn(createEmailRequest());
		when(mockMessagingClient.sendEmail(eq(MUNICIPALITY_ID), any())).thenReturn(new MessageResult());
		messagingIntegration.sendEmail(incident);

		verify(mockMessagingClient).sendEmail(eq(MUNICIPALITY_ID), any(EmailRequest.class));
		verify(mockMessagingMapper).toEmailDto(any());
	}

	@Test
	void sendMSVAEmail() {
		var incident = createIncidentEntity();
		incident.setMunicipalityId(MUNICIPALITY_ID);
		when(mockMessagingMapper.toMSVAEmailRequest(any())).thenReturn(createMSVAEmailRequest());
		when(mockMessagingClient.sendEmail(eq(MUNICIPALITY_ID), any())).thenReturn(new MessageResult());
		messagingIntegration.sendMSVAEmail(incident);

		verify(mockMessagingClient).sendEmail(eq(MUNICIPALITY_ID), any(EmailRequest.class));
		verify(mockMessagingMapper).toMSVAEmailRequest(any());
	}
}
