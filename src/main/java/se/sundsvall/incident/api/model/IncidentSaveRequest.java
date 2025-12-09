package se.sundsvall.incident.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import se.sundsvall.dept44.common.validators.annotation.ValidMobileNumber;
import se.sundsvall.incident.api.model.validation.ValidCoords;

@Getter
@Setter
@SuperBuilder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class IncidentSaveRequest {

	@Schema(description = "A uuid string representing a person", examples = "58f96da8-6d76-4fa6-bb92-64f71fdc6aa5", requiredMode = REQUIRED)
	private String personId;

	@Schema(description = "Mobile number. Should start with 07x", examples = "0701740605")
	@ValidMobileNumber(nullable = true)
	private String phoneNumber;

	@Email
	@Schema(description = "Reporters e-mail address", examples = "someemail@somehost.se")
	private String email;

	@Schema(description = "The way the reporter want to be contacted", examples = "email")
	private String contactMethod;

	@NotNull
	@Schema(description = "The category id for the incident ", examples = "15", requiredMode = REQUIRED)
	private Integer category;

	@NotBlank
	@Schema(description = "Description of the incident", examples = "A description", requiredMode = REQUIRED)
	private String description;

	@ValidCoords
	@Schema(description = "The map coordinates for the incident", examples = "62.23162,17.27403")
	private String mapCoordinates;

	@Schema(description = "The external case id for this incident", examples = "1234")
	private String externalCaseId;

	@Schema(description = "Attachments")
	private List<AttachmentRequest> attachments;

}
