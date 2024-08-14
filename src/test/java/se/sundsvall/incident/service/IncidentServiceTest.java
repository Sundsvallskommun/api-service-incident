package se.sundsvall.incident.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.zalando.problem.Status.BAD_REQUEST;
import static se.sundsvall.incident.TestDataFactory.INCIDENT_ID;
import static se.sundsvall.incident.TestDataFactory.MUNICIPALITY_ID;
import static se.sundsvall.incident.TestDataFactory.createCategoryEntity;
import static se.sundsvall.incident.TestDataFactory.createIncidentEntity;
import static se.sundsvall.incident.TestDataFactory.createIncidentSaveRequest;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.zalando.problem.Problem;

import se.sundsvall.incident.api.model.IncidentOepResponse;
import se.sundsvall.incident.api.model.IncidentResponse;
import se.sundsvall.incident.api.model.IncidentSaveResponse;
import se.sundsvall.incident.integration.db.entity.IncidentEntity;
import se.sundsvall.incident.integration.db.entity.enums.Status;
import se.sundsvall.incident.integration.db.repository.CategoryRepository;
import se.sundsvall.incident.integration.db.repository.IncidentRepository;
import se.sundsvall.incident.integration.lifebuoy.LifeBuoyIntegration;
import se.sundsvall.incident.integration.messaging.MessagingIntegration;

import generated.se.sundsvall.messaging.MessageResult;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

	@Mock
	private LifeBuoyIntegration mockLifeBuoyIntegration;

	@Mock
	private MessagingIntegration mockMessagingIntegration;

	@Mock
	private CategoryRepository mockCategoryRepository;

	@Mock
	private IncidentRepository mockIncidentRepository;

	@InjectMocks
	private IncidentService incidentService;

	@Test
	void fetchIncidentByMunicipalityIdAndIncidentIdTest() {
		when(mockIncidentRepository.findByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, INCIDENT_ID)).thenReturn(Optional.ofNullable(createIncidentEntity()));

		var result = incidentService.fetchIncidentByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, INCIDENT_ID);

		assertThat(result).isNotNull().isInstanceOf(IncidentResponse.class);
		verify(mockIncidentRepository).findByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, INCIDENT_ID);
		verifyNoMoreInteractions(mockIncidentRepository);
	}

	@Test
	void fetchIncidentByMunicipalityIdAndIncidentId_NotFoundTest() {
		when(mockIncidentRepository.findByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, INCIDENT_ID)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> incidentService.fetchIncidentByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, INCIDENT_ID))
			.hasMessageContaining("Not Found: Incident with id: " + INCIDENT_ID + " not found")
			.isInstanceOf(Problem.class);

		verify(mockIncidentRepository).findByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, INCIDENT_ID);
	}

	@Test
	void fetchOepIncidentStatusTest() {
		when(mockIncidentRepository.findIncidentEntityByMunicipalityIdAndExternalCaseId(MUNICIPALITY_ID, INCIDENT_ID))
			.thenReturn(Optional.ofNullable(createIncidentEntity()));

		var result = incidentService.fetchOepIncidentStatus(MUNICIPALITY_ID, INCIDENT_ID);

		assertThat(result).isNotNull().isInstanceOf(IncidentOepResponse.class);
		verify(mockIncidentRepository).findIncidentEntityByMunicipalityIdAndExternalCaseId(MUNICIPALITY_ID, INCIDENT_ID);
		verifyNoMoreInteractions(mockIncidentRepository);
	}

	@Test
	void fetchOepIncidentStatus_NotFoundTest() {
		when(mockIncidentRepository.findIncidentEntityByMunicipalityIdAndExternalCaseId(MUNICIPALITY_ID, INCIDENT_ID)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> incidentService.fetchOepIncidentStatus(MUNICIPALITY_ID, INCIDENT_ID))
			.hasMessageContaining("Not Found: Incident with id: " + INCIDENT_ID + " not found")
			.isInstanceOf(Problem.class);

		verify(mockIncidentRepository).findIncidentEntityByMunicipalityIdAndExternalCaseId(MUNICIPALITY_ID, INCIDENT_ID);
	}

	@Test
	void fetchPaginatedIncidentsTest() {
		var incidents = List.of(createIncidentEntity(), createIncidentEntity());
		Page<IncidentEntity> page = new PageImpl<>(incidents);
		when(mockIncidentRepository.findAllByMunicipalityId(MUNICIPALITY_ID, PageRequest.of(1, 2))).thenReturn(page);

		var result = incidentService.fetchPaginatedIncidents(MUNICIPALITY_ID, Optional.of(1), Optional.of(2));

		assertThat(result).hasSize(2);
		verify(mockIncidentRepository).findAllByMunicipalityId(eq(MUNICIPALITY_ID), any(PageRequest.class));
		verifyNoMoreInteractions(mockIncidentRepository);
	}

	@Test
	void createIncidentTest() {
		var request = createIncidentSaveRequest();
		when(mockCategoryRepository.findByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, request.getCategory())).thenReturn(Optional.ofNullable(createCategoryEntity()));

		var result = incidentService.createIncident(MUNICIPALITY_ID, request);

		assertThat(result).isNotNull().isInstanceOf(IncidentSaveResponse.class);
		verify(mockCategoryRepository).findByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, request.getCategory());
		verify(mockIncidentRepository).save(any());
		verifyNoMoreInteractions(mockCategoryRepository);
	}

	@Test
	void createIncidentNotFoundTest() {
		var request = createIncidentSaveRequest();
		when(mockCategoryRepository.findByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, request.getCategory())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> incidentService.createIncident(MUNICIPALITY_ID, request))
			.isInstanceOf(Problem.class)
			.hasMessageContaining("Bad Request: Category with id: ")
			.extracting("status").isEqualTo(BAD_REQUEST);

		verify(mockCategoryRepository).findByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, request.getCategory());
		verify(mockIncidentRepository, never()).save(any());
	}

	@Test
	void updateIncidentStatusTest() {
		var entity = createIncidentEntity();
		when(mockIncidentRepository.findByMunicipalityIdAndIncidentId(eq(MUNICIPALITY_ID), any())).thenReturn(Optional.ofNullable(entity));

		incidentService.updateIncidentStatus(MUNICIPALITY_ID, entity.getIncidentId(), 7);

		assertThat(entity.getStatus()).isEqualTo(Status.ARKIVERAD);
		verify(mockIncidentRepository).findByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, entity.getIncidentId());
		verify(mockIncidentRepository).save(entity);
		verifyNoMoreInteractions(mockIncidentRepository);
	}

	@Test
	void updateIncidentStatusNotFoundTest() {
		var entity = createIncidentEntity();
		when(mockIncidentRepository.findByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, entity.getIncidentId())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> incidentService.updateIncidentStatus(MUNICIPALITY_ID, entity.getIncidentId(), 1))
			.isInstanceOf(Problem.class)
			.hasMessageContaining("Not Found: Incident with id: ");

		assertThat(entity.getStatus()).isEqualTo(Status.INSKICKAT);
		verify(mockIncidentRepository).findByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, entity.getIncidentId());
		verify(mockIncidentRepository, never()).save(entity);
	}

	@Test
	void updateIncidentFeedbackTest() {
		var entity = createIncidentEntity();
		when(mockIncidentRepository.findByMunicipalityIdAndIncidentId(eq(MUNICIPALITY_ID), any())).thenReturn(Optional.ofNullable(entity));

		incidentService.updateIncidentFeedback(MUNICIPALITY_ID, entity.getIncidentId(), "Feedback!!");

		assertThat(entity.getFeedback()).isEqualTo("Feedback!!");
		verify(mockIncidentRepository).findByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, entity.getIncidentId());
		verify(mockIncidentRepository).save(entity);
		verifyNoMoreInteractions(mockIncidentRepository);
	}

	@Test
	void updateIncidentFeedbackNotFoundTest() {
		var entity = createIncidentEntity();
		when(mockIncidentRepository.findByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, entity.getIncidentId())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> incidentService.updateIncidentFeedback(MUNICIPALITY_ID, entity.getIncidentId(), "feedback"))
			.isInstanceOf(Problem.class)
			.hasMessageContaining("Not Found: Incident with id: ");

		verify(mockIncidentRepository).findByMunicipalityIdAndIncidentId(MUNICIPALITY_ID, entity.getIncidentId());
		verify(mockIncidentRepository, never()).save(entity);
	}

	@ParameterizedTest
	@ValueSource(strings = {"LIVBAT", "LIVBOJ"})
	void sendNotification_Lifebuoy_Test() throws JsonProcessingException {
		when(mockLifeBuoyIntegration.sendLifeBuoy(any())).thenReturn("nothing");
		var entity = createIncidentEntity();
		var category = createCategoryEntity();
		category.setTitle("LIVBAT");
		entity.setCategory(category);

		incidentService.sendNotification(entity);

		verify(mockLifeBuoyIntegration).sendLifeBuoy(any());
	}

	@ParameterizedTest
	@ValueSource(strings = {"VATTENMATARE", "BRADD_OVERVAKNINGS_LARM"})
	void sendNotificationTest(final String categoryTitle) {
		when(mockMessagingIntegration.sendMSVAEmail(any())).thenReturn(Optional.of(new MessageResult()));
		var entity = createIncidentEntity();
		var category = createCategoryEntity();
		category.setTitle(categoryTitle);
		entity.setCategory(category);

		incidentService.sendNotification(entity);

		verify(mockMessagingIntegration).sendMSVAEmail(entity);
	}

	@Test
	void sendNotification_WhenThrows_Test() throws JsonProcessingException {
		var entity = createIncidentEntity();
		var category = createCategoryEntity();
		category.setTitle("LIVBOJ");
		entity.setCategory(category);
		doThrow(new RuntimeException()).when(mockLifeBuoyIntegration).sendLifeBuoy(any());

		incidentService.sendNotification(entity);

		assertThat(entity.getStatus()).isEqualTo(Status.ERROR);
		verify(mockLifeBuoyIntegration).sendLifeBuoy(any());
	}

}
