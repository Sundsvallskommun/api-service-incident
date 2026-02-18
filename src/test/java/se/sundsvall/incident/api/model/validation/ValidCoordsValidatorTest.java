package se.sundsvall.incident.api.model.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ValidCoordsValidatorTest {

	@InjectMocks
	private ValidCoordsValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Test
	void isValidWithNullInput() {
		var result = validator.isValid(null, context);

		assertThat(result).isTrue();
	}

	@Test
	void isValidWithInvalidInput() {
		var result = validator.isValid("abc", context);

		assertThat(result).isFalse();
	}

	@Test
	void isValidWithValidInput() {
		var result = validator.isValid("62.4097,17.24024", context);

		assertThat(result).isTrue();
	}
}
