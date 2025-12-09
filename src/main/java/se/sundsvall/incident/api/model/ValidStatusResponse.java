package se.sundsvall.incident.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ValidStatusResponse {

	@Schema(description = "The ID of the status", examples = "5")
	private Integer statusId;

	@Schema(description = "The status name", examples = "INSKICKAT")
	private String status;

}
