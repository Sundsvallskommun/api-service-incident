package se.sundsvall.incident.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
import static se.sundsvall.incident.TestDataFactory.INCIDENT_ID;
import static se.sundsvall.incident.TestDataFactory.MUNICIPALITY_ID;
import static se.sundsvall.incident.TestDataFactory.createIncidentOepResponse;
import static se.sundsvall.incident.TestDataFactory.createIncidentResponse;
import static se.sundsvall.incident.TestDataFactory.createIncidentSaveRequest;
import static se.sundsvall.incident.TestDataFactory.createIncidentSaveResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import se.sundsvall.incident.integration.db.entity.enums.Status;
import se.sundsvall.incident.service.IncidentService;

@ExtendWith(MockitoExtension.class)
class IncidentResourceTest {

	@Mock
	private IncidentService mockIncidentService;

	@InjectMocks
	private IncidentResource incidentResource;

	@Test
	void fetchAllIncidents200WhenFoundTest() {
		when(mockIncidentService.fetchPaginatedIncidents(eq(MUNICIPALITY_ID), any(), any())).thenReturn(List.of(createIncidentResponse()));

		var response = incidentResource.fetchAllIncidents(MUNICIPALITY_ID, Optional.of(0), Optional.of(10));

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).hasSize(1);

		verify(mockIncidentService).fetchPaginatedIncidents(MUNICIPALITY_ID, Optional.of(0), Optional.of(10));
	}

	@Test
	void fetchAllIncidents200WhenEmptyTest() {
		when(mockIncidentService.fetchPaginatedIncidents(eq(MUNICIPALITY_ID), any(), any())).thenReturn(List.of());

		var response = incidentResource.fetchAllIncidents(MUNICIPALITY_ID, Optional.of(0), Optional.of(10));

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).isEmpty();

		verify(mockIncidentService).fetchPaginatedIncidents(MUNICIPALITY_ID, Optional.of(0), Optional.of(10));
	}

	@Test
	void fetchIncidentById200Test() {
		var incidentResponse = createIncidentResponse();
		when(mockIncidentService.fetchIncidentByMunicipalityIdAndIncidentId(eq(MUNICIPALITY_ID), any(String.class))).thenReturn(incidentResponse);

		var response = incidentResource.fetchIncidentById(MUNICIPALITY_ID, "123");

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody().getIncidentId()).isEqualTo(incidentResponse.getIncidentId());
		assertThat(response.getBody().getDescription()).isEqualTo(incidentResponse.getDescription());
		verify(mockIncidentService).fetchIncidentByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, "123");
	}

	@Test
	void getStatusForOeP() {
		when(mockIncidentService.fetchOepIncidentStatus(MUNICIPALITY_ID, INCIDENT_ID)).thenReturn(createIncidentOepResponse());
		final var response = incidentResource.getStatusForOeP(MUNICIPALITY_ID, INCIDENT_ID);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).isNotNull();
		verify(mockIncidentService).fetchOepIncidentStatus(MUNICIPALITY_ID, INCIDENT_ID);
	}

	@Test
	void createIncidentTest() {
		var request = createIncidentSaveRequest();
		when(mockIncidentService.createIncident(eq(MUNICIPALITY_ID), any())).thenReturn(createIncidentSaveResponse());

		var response = incidentResource.createIncident(MUNICIPALITY_ID, request);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).isNotNull();
		verify(mockIncidentService).createIncident(eq(MUNICIPALITY_ID), any());
	}

	@Test
	void updateIncidentStatusTest() {
		var uuid = UUID.randomUUID().toString();
		incidentResource.patchStatus(MUNICIPALITY_ID, uuid, 2);

		verify(mockIncidentService).updateIncidentStatus(MUNICIPALITY_ID, uuid, 2);
	}

	@Test
	void updateIncidentFeedbackTest() {
		var uuid = UUID.randomUUID().toString();
		incidentResource.patchFeedback(MUNICIPALITY_ID, uuid, "Bra jobbat");

		verify(mockIncidentService).updateIncidentFeedback(MUNICIPALITY_ID, uuid, "Bra jobbat");
	}

	@Test
	void getValidStatuses() {
		var response = incidentResource.getValidIncidentStatuses(MUNICIPALITY_ID);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).hasSize(Status.values().length);
		response.getBody()
			.forEach(validStatusResponse -> assertThat(validStatusResponse.getStatus())
				.isEqualTo(Status.forValue(validStatusResponse.getStatusId())
					.getLabel()));
	}

}
