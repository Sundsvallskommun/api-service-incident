package se.sundsvall.incident.integration.db.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.incident.TestDataFactory.buildAttachmentEntity;

import java.lang.reflect.Field;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;

class AttachmentEntityTest {

	@Test
	void testIdHasCorrectAnnotationsAndValues() {
		final Field id = FieldUtils.getDeclaredField(AttachmentEntity.class, "id", true);
		assertThat(id.getAnnotations()).hasSize(3);
		assertThat(id.getType()).isEqualTo(Integer.class);

		final Column column = id.getDeclaredAnnotation(Column.class);
		final GeneratedValue generatedValue = id.getDeclaredAnnotation(GeneratedValue.class);

		assertThat(column.name()).isEqualTo("ID");
		assertThat(generatedValue.strategy()).isEqualTo(GenerationType.AUTO);
	}

	@Test
	void testIncidentIdHasCorrectAnnotationsAndValues() {
		final Field incidentId = FieldUtils.getDeclaredField(AttachmentEntity.class, "incidentId", true);
		assertThat(incidentId.getAnnotations()).hasSize(1);
		assertThat(incidentId.getType()).isEqualTo(String.class);

		final Column column = incidentId.getDeclaredAnnotation(Column.class);
		assertThat(column.name()).isEqualTo("IncidentId");
	}

	@Test
	void testCategoryHasCorrectAnnotationsAndValues() {
		final Field category = FieldUtils.getDeclaredField(AttachmentEntity.class, "category", true);
		assertThat(category.getAnnotations()).hasSize(1);
		assertThat(category.getType()).isEqualTo(String.class);

		final Column column = category.getDeclaredAnnotation(Column.class);
		assertThat(column.name()).isEqualTo("category");
	}

	@Test
	void testExtensionHasCorrectAnnotationsAndValues() {
		final Field extension = FieldUtils.getDeclaredField(AttachmentEntity.class, "extension", true);
		assertThat(extension.getAnnotations()).hasSize(1);
		assertThat(extension.getType()).isEqualTo(String.class);

		final Column column = extension.getDeclaredAnnotation(Column.class);
		assertThat(column.name()).isEqualTo("extension");
	}

	@Test
	void testMimeTypeHasCorrectAnnotationsAndValues() {
		final Field mimeType = FieldUtils.getDeclaredField(AttachmentEntity.class, "mimeType", true);
		assertThat(mimeType.getAnnotations()).hasSize(1);
		assertThat(mimeType.getType()).isEqualTo(String.class);

		final Column column = mimeType.getDeclaredAnnotation(Column.class);
		assertThat(column.name()).isEqualTo("mimetype");
	}

	@Test
	void testNoteHasCorrectAnnotationsAndValues() {
		final Field note = FieldUtils.getDeclaredField(AttachmentEntity.class, "note", true);
		assertThat(note.getAnnotations()).hasSize(1);
		assertThat(note.getType()).isEqualTo(String.class);

		final Column column = note.getDeclaredAnnotation(Column.class);
		assertThat(column.name()).isEqualTo("note");
	}

	@Test
	void testFileHasCorrectAnnotationsAndValues() {
		final Field file = FieldUtils.getDeclaredField(AttachmentEntity.class, "file", true);
		assertThat(file.getAnnotations()).hasSize(1);
		assertThat(file.getType()).isEqualTo(String.class);

		final Column column = file.getDeclaredAnnotation(Column.class);
		assertThat(column.name()).isEqualTo("file");
	}

	@Test
	void testNameHasCorrectAnnotationsAndValues() {
		final Field name = FieldUtils.getDeclaredField(AttachmentEntity.class, "name", true);
		assertThat(name.getAnnotations()).hasSize(1);
		assertThat(name.getType()).isEqualTo(String.class);

		final Column column = name.getDeclaredAnnotation(Column.class);
		assertThat(column.name()).isEqualTo("name");
	}

	@Test
	void testCreatedHasCorrectAnnotationsAndValues() {
		final Field created = FieldUtils.getDeclaredField(AttachmentEntity.class, "created", true);
		assertThat(created.getAnnotations()).hasSize(1);
		assertThat(created.getType()).isEqualTo(String.class);

		final Column column = created.getDeclaredAnnotation(Column.class);
		assertThat(column.name()).isEqualTo("Created");
	}

	@Test
	void getterTest() {
		var entity = buildAttachmentEntity();
		var id = entity.getId();
		var name = entity.getName();
		var mimeType = entity.getMimeType();
		var note = entity.getNote();
		var extension = entity.getExtension();
		var file = entity.getFile();
		var category = entity.getCategory();
		var incidentId = entity.getIncidentId();

		assertThat(id).satisfies(i -> {
			assertThat(i).isNotNull();
			assertThat(i).isEqualTo(entity.getId());
		});
		assertThat(name).satisfies(n -> {
			assertThat(n).isNotNull();
			assertThat(n).isEqualTo(entity.getName());
		});
		assertThat(mimeType).satisfies(mt -> {
			assertThat(mt).isNotNull();
			assertThat(mt).isEqualTo(entity.getMimeType());
		});
		assertThat(note).satisfies(n -> {
			assertThat(n).isNotNull();
			assertThat(n).isEqualTo(entity.getNote());
		});
		assertThat(extension).satisfies(e -> {
			assertThat(e).isNotNull();
			assertThat(e).isEqualTo(entity.getExtension());
		});
		assertThat(file).satisfies(f -> {
			assertThat(f).isNotNull();
			assertThat(f).isEqualTo(entity.getFile());
		});
		assertThat(category).satisfies(c -> {
			assertThat(c).isNotNull();
			assertThat(c).isEqualTo(entity.getCategory());
		});
		assertThat(incidentId).satisfies(i -> {
			assertThat(i).isNotNull();
			assertThat(i).isEqualTo(entity.getIncidentId());
		});
	}

}
