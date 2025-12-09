package se.sundsvall.incident.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CategoryPost(

	@NotBlank @Schema(description = "Category name", examples = "GANGCYKELVAG", requiredMode = Schema.RequiredMode.REQUIRED) String title,

	@NotBlank @Schema(description = "Description of the category", examples = "Gång- och cykelväg", requiredMode = Schema.RequiredMode.REQUIRED) String label,

	@NotBlank @Email @Schema(description = "The E-mail where the incidents are forwarded to", examples = "nowhere@nowhere.com", requiredMode = Schema.RequiredMode.REQUIRED) String forwardTo,

	@NotBlank @Schema(description = "The subject of the email", examples = "Nytt larm", requiredMode = Schema.RequiredMode.REQUIRED) String subject) {
}
