package se.sundsvall.incident.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.incident.TestDataFactory.MUNICIPALITY_ID;
import static se.sundsvall.incident.TestDataFactory.createCategoryEntity;
import static se.sundsvall.incident.TestDataFactory.createCategoryPatch;
import static se.sundsvall.incident.TestDataFactory.createCategoryPost;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Problem;
import se.sundsvall.incident.api.model.Category;
import se.sundsvall.incident.api.model.ValidCategoryResponse;
import se.sundsvall.incident.api.model.ValidOepCategoryResponse;
import se.sundsvall.incident.integration.db.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

	@Mock
	private CategoryRepository mockCategoryRepository;

	@InjectMocks
	private CategoryService categoryService;

	@Test
	void fetchCategoryByMunicipalityAndIdTest() {
		when(mockCategoryRepository.findByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, 5))
			.thenReturn(Optional.of(createCategoryEntity()));

		var result = categoryService.fetchCategoryByMunicipalityAndId(MUNICIPALITY_ID, 5);

		assertThat(result).isInstanceOf(Category.class).isNotNull();

		verify(mockCategoryRepository).findByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, 5);
		verifyNoMoreInteractions(mockCategoryRepository);
	}

	@Test
	void fetchCategoryByMunicipalityAndIdNotFoundTest() {
		when(mockCategoryRepository.findByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, 5))
			.thenReturn(Optional.empty());

		assertThatThrownBy(() -> categoryService.fetchCategoryByMunicipalityAndId(MUNICIPALITY_ID, 5))
			.isInstanceOf(Problem.class)
			.hasMessageContaining("Not Found: Category");

		verify(mockCategoryRepository).findByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, 5);
		verifyNoMoreInteractions(mockCategoryRepository);
	}

	@Test
	void fetchAllCategoryWhenFound() {
		when(mockCategoryRepository.findAllByMunicipalityId(MUNICIPALITY_ID)).thenReturn(List.of(createCategoryEntity(), createCategoryEntity()));

		final var categories = categoryService.fetchCategories(MUNICIPALITY_ID);

		assertThat(categories).isNotNull().isNotEmpty().hasSize(2);

		verify(mockCategoryRepository).findAllByMunicipalityId(MUNICIPALITY_ID);
		verifyNoMoreInteractions(mockCategoryRepository);
	}

	@Test
	void fetchCategoriesWhenEmpty() {
		when(mockCategoryRepository.findAllByMunicipalityId(MUNICIPALITY_ID)).thenReturn(List.of());

		final var categories = categoryService.fetchCategories(MUNICIPALITY_ID);

		assertThat(categories).isNotNull().isEmpty();

		verify(mockCategoryRepository).findAllByMunicipalityId(MUNICIPALITY_ID);
		verifyNoMoreInteractions(mockCategoryRepository);
	}

	@Test
	void createCategoryTest() {
		final var entity = createCategoryEntity();
		when(mockCategoryRepository.save(any())).thenReturn(entity);

		final var category = categoryService.createCategory(MUNICIPALITY_ID, createCategoryPost());

		assertThat(category.getCategoryId()).isEqualTo(entity.getCategoryId());
		assertThat(category.getForwardTo()).isEqualTo(entity.getForwardTo());
		assertThat(category.getTitle()).isEqualTo(entity.getTitle());

		verify(mockCategoryRepository).save(any());
		verifyNoMoreInteractions(mockCategoryRepository);
	}

	@Test
	void deleteCategoryByIdTest() {
		when(mockCategoryRepository.existsByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, 5)).thenReturn(true);

		categoryService.deleteCategoryById(MUNICIPALITY_ID, 5);
		verify(mockCategoryRepository).existsByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, 5);
		verify(mockCategoryRepository).deleteByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, 5);
	}

	@Test
	void deleteCategoryByIdNotFoundTest() {
		when(mockCategoryRepository.existsByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, 5)).thenReturn(false);

		assertThatThrownBy(() -> categoryService.deleteCategoryById(MUNICIPALITY_ID, 5))
			.isInstanceOf(Problem.class)
			.hasMessageContaining("Not Found: Category");

		verify(mockCategoryRepository, never()).deleteByMunicipalityIdAndCategoryId(any(), any());
	}

	@Test
	void patchEntityTest() {
		var entity = createCategoryEntity();
		when(mockCategoryRepository.findByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, 5)).thenReturn(Optional.ofNullable(entity));
		when(mockCategoryRepository.save(any())).thenReturn(entity);

		var result = categoryService.patchCategory(MUNICIPALITY_ID, 5, createCategoryPatch());

		assertThat(result).isInstanceOf(Category.class);
		verify(mockCategoryRepository).findByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, 5);
		verify(mockCategoryRepository).save(any());
	}

	@Test
	void patchEntityNotFoundTest() {
		var patch = createCategoryPatch();
		when(mockCategoryRepository.findByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, 5)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> categoryService.patchCategory(MUNICIPALITY_ID, 5, patch))
			.isInstanceOf(Problem.class)
			.hasMessageContaining("Not Found: Category");

		verify(mockCategoryRepository).findByMunicipalityIdAndCategoryId(MUNICIPALITY_ID, 5);
		verify(mockCategoryRepository, never()).save(any());
	}

	@Test
	void fetchValidCategoriesTest() {
		when(mockCategoryRepository.findAllByMunicipalityId(MUNICIPALITY_ID)).thenReturn(List.of(createCategoryEntity(), createCategoryEntity()));

		var result = categoryService.fetchValidCategories(MUNICIPALITY_ID);

		assertThat(result).hasSize(2);
		assertThat(result.getFirst()).isInstanceOf(ValidCategoryResponse.class);
		verify(mockCategoryRepository).findAllByMunicipalityId(MUNICIPALITY_ID);
		verifyNoMoreInteractions(mockCategoryRepository);
	}

	@Test
	void fetchValidOepCategoriesTest() {
		when(mockCategoryRepository.findAllByMunicipalityId(MUNICIPALITY_ID)).thenReturn(List.of(createCategoryEntity(), createCategoryEntity()));

		var result = categoryService.fetchValidOepCategories(MUNICIPALITY_ID);

		assertThat(result).hasSize(2);
		assertThat(result.getFirst()).isInstanceOf(ValidOepCategoryResponse.class);
		verify(mockCategoryRepository).findAllByMunicipalityId(MUNICIPALITY_ID);
		verifyNoMoreInteractions(mockCategoryRepository);
	}

}
