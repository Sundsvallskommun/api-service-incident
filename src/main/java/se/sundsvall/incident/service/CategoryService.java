package se.sundsvall.incident.service;

import static java.util.Optional.ofNullable;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.incident.service.mapper.Mapper.toCategory;
import static se.sundsvall.incident.service.mapper.Mapper.toCategoryEntity;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import se.sundsvall.incident.api.model.Category;
import se.sundsvall.incident.api.model.CategoryPatch;
import se.sundsvall.incident.api.model.CategoryPost;
import se.sundsvall.incident.api.model.ValidCategoryResponse;
import se.sundsvall.incident.api.model.ValidOepCategoryResponse;
import se.sundsvall.incident.integration.db.entity.CategoryEntity;
import se.sundsvall.incident.integration.db.repository.CategoryRepository;
import se.sundsvall.incident.service.mapper.Mapper;

@Service
public class CategoryService {

	private static final String CATEGORY_NOT_FOUND = "Category with id: %s not found";
	private final CategoryRepository categoryRepository;

	public CategoryService(final CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Category fetchCategoryByMunicipalityAndId(final String municipalityId, final Integer categoryId) {
		var category = categoryRepository.findByMunicipalityIdAndCategoryId(municipalityId, categoryId)
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND, CATEGORY_NOT_FOUND.formatted(categoryId)));
		return toCategory(category);
	}

	public List<Category> fetchCategories(final String municipalityId) {
		return categoryRepository.findAllByMunicipalityId(municipalityId).stream()
			.map(Mapper::toCategory)
			.toList();
	}

	public Category createCategory(final String municipalityId, final CategoryPost categoryPost) {
		var category = categoryRepository.save(toCategoryEntity(municipalityId, categoryPost));
		return toCategory(category);
	}

	public void deleteCategoryById(final String municipalityId, final Integer categoryId) {
		if (!categoryRepository.existsByMunicipalityIdAndCategoryId(municipalityId, categoryId)) {
			throw Problem.valueOf(NOT_FOUND, CATEGORY_NOT_FOUND.formatted(categoryId));
		}
		categoryRepository.deleteByMunicipalityIdAndCategoryId(municipalityId, categoryId);
	}

	public Category patchCategory(final String municipalityId, final Integer id, final CategoryPatch patch) {
		var categoryEntity = categoryRepository.findByMunicipalityIdAndCategoryId(municipalityId, id)
			.orElseThrow(() -> Problem.valueOf(NOT_FOUND, CATEGORY_NOT_FOUND.formatted(id)));
		var category = categoryRepository.save(patchCategory(categoryEntity, patch));
		return toCategory(category);
	}

	public List<ValidCategoryResponse> fetchValidCategories(final String municipalityId) {
		return categoryRepository.findAllByMunicipalityId(municipalityId).stream()
			.map(category -> ValidCategoryResponse.builder()
				.withCategory(category.getLabel())
				.withCategoryId(category.getCategoryId())
				.build())
			.toList();
	}

	public List<ValidOepCategoryResponse> fetchValidOepCategories(final String municipalityId) {
		return categoryRepository.findAllByMunicipalityId(municipalityId).stream()
			.map(category -> ValidOepCategoryResponse.builder()
				.withKey(String.valueOf(category.getCategoryId()))
				.withValue(category.getLabel())
				.build())
			.toList();
	}

	CategoryEntity patchCategory(final CategoryEntity entity, final CategoryPatch patch) {
		ofNullable(patch.title()).ifPresent(entity::setTitle);
		ofNullable(patch.label()).ifPresent(entity::setLabel);
		ofNullable(patch.forwardTo()).ifPresent(entity::setForwardTo);
		return entity;
	}
}
