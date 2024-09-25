package se.sundsvall.incident.service.mapper;

import static java.util.UUID.randomUUID;

import java.util.List;
import java.util.Optional;

import se.sundsvall.incident.api.model.Attachment;
import se.sundsvall.incident.api.model.AttachmentRequest;
import se.sundsvall.incident.api.model.Category;
import se.sundsvall.incident.api.model.CategoryPost;
import se.sundsvall.incident.api.model.IncidentOepResponse;
import se.sundsvall.incident.api.model.IncidentResponse;
import se.sundsvall.incident.api.model.IncidentSaveRequest;
import se.sundsvall.incident.integration.db.entity.AttachmentEntity;
import se.sundsvall.incident.integration.db.entity.CategoryEntity;
import se.sundsvall.incident.integration.db.entity.IncidentEntity;
import se.sundsvall.incident.integration.db.entity.enums.Status;

public final class Mapper {

	private Mapper() {
		// Never instantiate
	}

	public static IncidentEntity toIncidentEntity(final IncidentSaveRequest request, final CategoryEntity category, final List<AttachmentEntity> attachments, final String municipalityId) {
		return Optional.ofNullable(request).map(req -> IncidentEntity.builder()
			.withPersonId(req.getPersonId())
			.withExternalCaseId(req.getExternalCaseId())
			.withDescription(req.getDescription())
			.withEmail(req.getEmail())
			.withCoordinates(req.getMapCoordinates())
			.withContactMethod(req.getContactMethod())
			.withPhoneNumber(req.getPhoneNumber())
			.withMunicipalityId(municipalityId)
			.withStatus(Status.INSKICKAT)
			.withCategory(category)
			.withAttachments(attachments)
			.build())
			.orElse(null);
	}

	public static IncidentResponse toIncidentResponse(final IncidentEntity entity) {
		return Optional.ofNullable(entity).map(e -> IncidentResponse.builder()
			.withIncidentId(e.getIncidentId())
			.withPersonId(e.getPersonId())
			.withExternalCaseId(e.getExternalCaseId())
			.withDescription(e.getDescription())
			.withPhoneNumber(e.getPhoneNumber())
			.withContactMethod(e.getContactMethod())
			.withEmail(e.getEmail())
			.withDescription(e.getDescription())
			.withUpdated(e.getUpdated())
			.withCreated(e.getCreated())
			.withStatus(e.getStatus())
			.withCategory(toCategory(e.getCategory()))
			.withAttachments(e.getAttachments().stream()
				.map(Mapper::toAttachment)
				.toList())
			.build())
			.orElse(null);
	}

	public static IncidentOepResponse toIncidentOepResponse(final IncidentEntity entity) {
		return Optional.ofNullable(entity).map(e -> IncidentOepResponse.builder()
			.withIncidentId(e.getIncidentId())
			.withStatusId(e.getStatus().getValue())
			.withStatusText(e.getStatus().getLabel())
			.withExternalCaseId(e.getExternalCaseId())
			.build())
			.orElse(null);
	}

	public static Category toCategory(final CategoryEntity entity) {
		return Optional.ofNullable(entity).map(e -> Category.builder()
			.withCategoryId(e.getCategoryId())
			.withTitle(e.getTitle())
			.withLabel(e.getLabel())
			.withForwardTo(e.getForwardTo())
			.withSubject(e.getSubject())
			.build())
			.orElse(null);
	}

	public static CategoryEntity toCategoryEntity(final String municipalityId, final CategoryPost categoryPost) {
		return Optional.ofNullable(categoryPost).map(category -> CategoryEntity.builder()
			.withMunicipalityId(municipalityId)
			.withLabel(category.label())
			.withTitle(category.title())
			.withForwardTo(category.forwardTo())
			.withSubject(category.subject())
			.build())
			.orElse(null);
	}

	public static Attachment toAttachment(final AttachmentEntity entity) {
		return Optional.ofNullable(entity).map(e -> Attachment.builder()
			.withName(e.getName())
			.withMimeType(e.getMimeType())
			.withNote(e.getNote())
			.withExtension(e.getExtension())
			.withFile(e.getFile())
			.withCategory(e.getCategory())
			.build())
			.orElse(null);
	}

	public static AttachmentEntity toAttachmentEntity(final AttachmentRequest request) {
		return Optional.ofNullable(request).map(req -> AttachmentEntity.builder()
			.withMimeType(req.getMimeType())
			.withNote(req.getNote())
			.withExtension(req.getExtension())
			.withFile(req.getFile())
			.withCategory(req.getCategory())
			.withName(randomUUID().toString())
			.build())
			.orElse(null);
	}
}
