package se.sundsvall.incident.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Attachment {

	@Schema(description = "The category label", examples = "Vattenmätare")
	private String category;

	@Schema(description = "The name of the attachment", examples = "Vattenmätningar")
	private String name;

	@Schema(description = "The attachment extension", examples = ".pdf")
	private String extension;

	@Schema(description = "The attachment MIME type", examples = "application/json")
	private String mimeType;

	@Schema(description = "The attachment note", examples = "Beskrivande meddelande")
	private String note;

	@Schema(description = "The file content as base64 encoded string", examples = "aGVqaGVq")
	private String file;

}
