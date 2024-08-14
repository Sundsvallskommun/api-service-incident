package se.sundsvall.incident.integration.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import se.sundsvall.incident.integration.db.entity.CategoryEntity;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "categoryRepository")
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

	List<CategoryEntity> findAllByMunicipalityId(final String municipalityId);

	Optional<CategoryEntity> findByMunicipalityIdAndCategoryId(final String municipalityId, final Integer categoryId);

	boolean existsByMunicipalityIdAndCategoryId(final String municipalityId, final Integer categoryId);

	void deleteByMunicipalityIdAndCategoryId(final String municipalityId, final Integer id);
}
