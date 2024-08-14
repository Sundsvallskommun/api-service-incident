package se.sundsvall.incident.integration.db.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import se.sundsvall.incident.integration.db.entity.IncidentEntity;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "incidentRepository")
public interface IncidentRepository extends JpaRepository<IncidentEntity, String> {

	Optional<IncidentEntity> findIncidentEntityByMunicipalityIdAndExternalCaseId(final String municipalityId, final String externalCaseId);

	Page<IncidentEntity> findAllByMunicipalityId(final String municipalityId, final Pageable pageable);

	Optional<IncidentEntity> findByMunicipalityIdAndIncidentId(final String municipalityId, final String incidentId);
}
