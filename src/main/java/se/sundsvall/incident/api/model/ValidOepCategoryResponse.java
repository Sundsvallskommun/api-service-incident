package se.sundsvall.incident.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ValidOepCategoryResponse {

	@Schema(description = "The category ID", examples = "15")
	private String key;

	@Schema(description = "The category label", examples = "VATTENMÄTARE")
	private String value;

}
