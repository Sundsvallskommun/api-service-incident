package se.sundsvall.incident.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.incident.api.model.IncidentOepResponse;
import se.sundsvall.incident.api.model.IncidentResponse;
import se.sundsvall.incident.api.model.IncidentSaveRequest;
import se.sundsvall.incident.api.model.IncidentSaveResponse;
import se.sundsvall.incident.api.model.ValidStatusResponse;
import se.sundsvall.incident.integration.db.entity.enums.Status;
import se.sundsvall.incident.service.IncidentService;

@RestController
@Validated
@RequestMapping("/{municipalityId}/incident")
@Tag(name = "Incident resources")
@ApiResponses(value = {
	@ApiResponse(responseCode = "400",
		description = "Bad Request",
		content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
			Problem.class, ConstraintViolationProblem.class
		}))),
	@ApiResponse(responseCode = "500",
		description = "Internal Server Error",
		content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
})
class IncidentResource {

	private final IncidentService incidentService;

	IncidentResource(final IncidentService incidentService) {
		this.incidentService = incidentService;
	}

	@Operation(summary = "Get list of incidents", responses = {
		@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true)
	})
	@GetMapping(produces = {
		APPLICATION_PROBLEM_JSON_VALUE, APPLICATION_JSON_VALUE
	})
	ResponseEntity<List<IncidentResponse>> fetchAllIncidents(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId,
		@RequestParam(required = false) final Optional<Integer> pageNumber,
		@RequestParam(required = false) final Optional<Integer> pageSize) {

		return ok(incidentService.fetchPaginatedIncidents(municipalityId, pageNumber, pageSize));
	}

	@Operation(summary = "Get a incident by id",
		responses = {
			@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404",
				description = "Not Found",
				content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
		})
	@GetMapping(path = "/{id}", produces = {
		APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE
	})
	ResponseEntity<IncidentResponse> fetchIncidentById(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId,
		@PathVariable("id") final String id) {

		return ok(incidentService.fetchIncidentByMunicipalityIdAndIncidentId(municipalityId, id));
	}

	@Operation(summary = "Get status for a OeP incident",
		responses = {
			@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404",
				description = "Not Found",
				content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
		})
	@GetMapping(path = "/internal/oep/{externalCaseId}/status")
	ResponseEntity<IncidentOepResponse> getStatusForOeP(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId,
		@PathVariable("externalCaseId") final String externalCaseId) {

		return ok(incidentService.fetchOepIncidentStatus(municipalityId, externalCaseId));
	}

	@Operation(summary = "Get a list of valid statuses", responses = {
		@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true)

	})
	@GetMapping(value = "/incident/statuses", produces = {
		APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE
	})
	public ResponseEntity<List<ValidStatusResponse>> getValidIncidentStatuses(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId) {
		return ok(Arrays.stream(Status.values())
			.map(dto -> ValidStatusResponse.builder()
				.withStatus(dto.getLabel())
				.withStatusId(dto.getValue()).build())
			.toList());
	}

	@Operation(summary = "Create a incident and send notification", responses = {
		@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true)
	})
	@PostMapping(produces = {
		APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE
	}, consumes = APPLICATION_JSON_VALUE)
	ResponseEntity<IncidentSaveResponse> createIncident(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId,
		@RequestBody @Valid final IncidentSaveRequest incidentSaveRequest) {

		return ok(incidentService.createIncident(municipalityId, incidentSaveRequest));
	}

	@Operation(summary = "Update the status for a specific incident",
		responses = {
			@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404",
				description = "Not Found",
				content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
		})
	@PatchMapping(path = "/status/{incidentId}", produces = APPLICATION_PROBLEM_JSON_VALUE)
	ResponseEntity<Void> patchStatus(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId,
		@PathVariable("incidentId") final String incidentId,
		@RequestParam("status") final Integer statusId) {

		incidentService.updateIncidentStatus(municipalityId, incidentId, statusId);
		return ok().build();
	}

	@Operation(summary = "Set incident feedback",
		responses = {
			@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404",
				description = "Not Found",
				content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
		})
	@PatchMapping(path = "/feedback/{incidentId}", produces = APPLICATION_PROBLEM_JSON_VALUE)
	ResponseEntity<Void> patchFeedback(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId,
		@PathVariable("incidentId") final String incidentId,
		@RequestParam("feedback") final String feedback) {

		incidentService.updateIncidentFeedback(municipalityId, incidentId, feedback);
		return ok().build();
	}
}
