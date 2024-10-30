package se.sundsvall.incident.api;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.incident.api.model.Category;
import se.sundsvall.incident.api.model.CategoryPatch;
import se.sundsvall.incident.api.model.CategoryPost;
import se.sundsvall.incident.api.model.ValidCategoryResponse;
import se.sundsvall.incident.api.model.ValidOepCategoryResponse;
import se.sundsvall.incident.service.CategoryService;

@RestController
@Validated
@RequestMapping("/{municipalityId}/category")
@Tag(name = "Category resources")
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
class CategoryResource {

	private final CategoryService categoryService;

	CategoryResource(final CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Operation(summary = "Fetch all categories",
		responses = {
			@ApiResponse(responseCode = "200", description = "All categories returned", useReturnTypeSchema = true),
		})
	@GetMapping(produces = {
		APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE
	})
	public ResponseEntity<List<Category>> getAllCategories(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId) {

		return ok(categoryService.fetchCategories(municipalityId));
	}

	@Operation(summary = "Fetch category by ID",
		responses = {
			@ApiResponse(responseCode = "200", description = "Category returned", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404",
				description = "Not found",
				content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
		})
	@GetMapping(path = "/{id}", produces = {
		APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE
	})
	public ResponseEntity<Category> getCategoryById(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId,
		@PathVariable("id") final Integer id) {

		return ok(categoryService.fetchCategoryByMunicipalityAndId(municipalityId, id));
	}

	@Operation(summary = "Create category",
		responses = {
			@ApiResponse(responseCode = "201", description = "Category created", useReturnTypeSchema = true),
		})
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_PROBLEM_JSON_VALUE)
	public ResponseEntity<Void> postCategory(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId,
		@RequestBody @Valid final CategoryPost categoryPost) {

		final var createdCategory = categoryService.createCategory(municipalityId, categoryPost);
		return created(fromPath("category/{id}").buildAndExpand(createdCategory.getCategoryId()).toUri())
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@Operation(summary = "Update a category",
		responses = {
			@ApiResponse(responseCode = "200", description = "Category was updated", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404",
				description = "Not found",
				content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
		})
	@PatchMapping(path = "/{id}", produces = {
		APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE
	}, consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> patchCategory(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId,
		@PathVariable("id") final Integer id,
		@RequestBody @Valid final CategoryPatch patch) {

		return ok(categoryService.patchCategory(municipalityId, id, patch));
	}

	@Operation(summary = "Delete a category by id",
		responses = {
			@ApiResponse(responseCode = "204", description = "Category deleted", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404",
				description = "Not found",
				content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
		})
	@DeleteMapping(path = "/{id}", produces = APPLICATION_PROBLEM_JSON_VALUE)
	public ResponseEntity<Void> deleteCategoryById(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId,
		@PathVariable("id") final Integer id) {

		categoryService.deleteCategoryById(municipalityId, id);
		return noContent()
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@Operation(summary = "Get a list of valid categories")
	@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true)
	@GetMapping(path = "/valid", produces = {
		APPLICATION_PROBLEM_JSON_VALUE, APPLICATION_JSON_VALUE
	})
	public ResponseEntity<List<ValidCategoryResponse>> getValidCategories(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId) {

		return ok(categoryService.fetchValidCategories(municipalityId));
	}

	@Operation(summary = "Get a list of valid categories in oep format")
	@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true)
	@GetMapping(path = "/valid/oep", produces = {
		APPLICATION_PROBLEM_JSON_VALUE, APPLICATION_JSON_VALUE
	})
	public ResponseEntity<List<ValidOepCategoryResponse>> getValidOepCategories(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @PathVariable("municipalityId") @ValidMunicipalityId final String municipalityId) {

		return ok(categoryService.fetchValidOepCategories(municipalityId));
	}
}
