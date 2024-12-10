package se.sundsvall.incident.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static se.sundsvall.incident.TestDataFactory.MUNICIPALITY_ID;
import static se.sundsvall.incident.TestDataFactory.createCategoryDTO;
import static se.sundsvall.incident.TestDataFactory.createCategoryPatch;
import static se.sundsvall.incident.TestDataFactory.createCategoryPost;
import static se.sundsvall.incident.TestDataFactory.createValidCategoryResponse;
import static se.sundsvall.incident.TestDataFactory.createValidOepCategoryResponse;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import se.sundsvall.incident.service.CategoryService;

@ExtendWith(MockitoExtension.class)
class CategoryResourceTest {

	@Mock
	private CategoryService mockCategoryService;

	@InjectMocks
	private CategoryResource categoryResource;

	@Test
	void getAllCategoriesTest_shouldReturn200() {
		when(mockCategoryService.fetchCategories(MUNICIPALITY_ID)).thenReturn(List.of(createCategoryDTO()));

		var response = categoryResource.getAllCategories(MUNICIPALITY_ID);

		assertThat(response.getStatusCode()).isEqualTo(OK);
	}

	@Test
	void getCategoryByIdTest_shouldReturn200_whenFound() {
		when(mockCategoryService.fetchCategoryByMunicipalityAndId(eq(MUNICIPALITY_ID), any(Integer.class))).thenReturn(createCategoryDTO());

		var response = categoryResource.getCategoryById(MUNICIPALITY_ID, 5);

		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).isNotNull();
	}

	@Test
	void postCategoryTest_shouldReturn201_whenCreated() {
		when(mockCategoryService.createCategory(eq(MUNICIPALITY_ID), any())).thenReturn(createCategoryDTO());

		var response = categoryResource.postCategory(MUNICIPALITY_ID, createCategoryPost());

		assertThat(response.getStatusCode()).isEqualTo(CREATED);
	}

	@Test
	void patchCategoryTest_shouldReturn200_whenOk() {
		when(mockCategoryService.patchCategory(eq(MUNICIPALITY_ID), any(), any())).thenReturn(createCategoryDTO());

		var response = categoryResource.patchCategory(MUNICIPALITY_ID, 5, createCategoryPatch());

		assertThat(response.getStatusCode()).isEqualTo(OK);
	}

	@Test
	void deleteCategoryByIdTest_shouldReturn204_whenDeleted() {
		var response = categoryResource.deleteCategoryById(MUNICIPALITY_ID, 5);

		assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
	}

	@Test
	void getValidCategories() {
		when(mockCategoryService.fetchValidCategories(MUNICIPALITY_ID)).thenReturn(List.of(createValidCategoryResponse()));

		var response = categoryResource.getValidCategories(MUNICIPALITY_ID);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).hasSize(1);

		verify(mockCategoryService).fetchValidCategories(MUNICIPALITY_ID);
		verifyNoMoreInteractions(mockCategoryService);
	}

	@Test
	void getValidOepCategories() {
		when(mockCategoryService.fetchValidOepCategories(MUNICIPALITY_ID)).thenReturn(List.of(createValidOepCategoryResponse()));

		var response = categoryResource.getValidOepCategories(MUNICIPALITY_ID);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull().hasSize(1);
		verify(mockCategoryService).fetchValidOepCategories(MUNICIPALITY_ID);
		verifyNoMoreInteractions(mockCategoryService);
	}

}
