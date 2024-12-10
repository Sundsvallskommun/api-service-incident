package se.sundsvall.incident.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import se.sundsvall.incident.api.model.validation.NullOrNotBlank;

public record CategoryPatch(

	@NullOrNotBlank @Schema(description = "Category title") String title,

	@NullOrNotBlank @Schema(description = "Category label") String label,

	@Email @Schema(description = "The E-mail where the incidents are forwarded to") String forwardTo) {
}
