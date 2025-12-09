package se.sundsvall.incident.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Schema(description = "Category model")
public class Category {

	@Schema(description = "The category id", examples = "15")
	private Integer categoryId;

	@Schema(description = "Category name", examples = "GANGCYKELVAG")
	private String title;

	@Schema(description = "Description of the category", examples = "Gång- och cykelväg")
	private String label;

	@Schema(description = "The E-mail where the incidents are forwarded to", examples = "nowhere@nowhere.com")
	private String forwardTo;

	@Schema(description = "The E-mail subject", examples = "Nytt Larm")
	private String subject;

}
