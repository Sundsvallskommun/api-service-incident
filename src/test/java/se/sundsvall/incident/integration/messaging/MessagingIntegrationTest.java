package se.sundsvall.incident.integration.messaging;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.sundsvall.incident.TestDataFactory.buildEmailRequest;
import static se.sundsvall.incident.TestDataFactory.buildIncidentDto;
import static se.sundsvall.incident.TestDataFactory.buildMSVAEmailRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import generated.se.sundsvall.messaging.EmailRequest;

@ExtendWith(MockitoExtension.class)
class MessagingIntegrationTest {

	@Mock
	private EmailMapper emailMapper;

	@Mock
	private MessagingClient messagingClient;

	@InjectMocks
	private MessagingIntegration messagingIntegration;

	@Test
	void sendEmail() {
		var incidentDto = buildIncidentDto();
		when(emailMapper.toEmailDto(any())).thenReturn(buildEmailRequest());
		messagingIntegration.sendEmail(incidentDto);

		verify(messagingClient, times(1)).sendEmail(any(EmailRequest.class));
		verify(emailMapper, times(1)).toEmailDto(any());
	}

	@Test
	void sendMSVAEmail() {
		var incidentDto = buildIncidentDto();
		when(emailMapper.toMSVAEmailDto(any())).thenReturn(buildMSVAEmailRequest());
		messagingIntegration.sendMSVAEmail(incidentDto);

		verify(messagingClient, times(1)).sendEmail(any(EmailRequest.class));
		verify(emailMapper, times(1)).toMSVAEmailDto(any());
	}
}