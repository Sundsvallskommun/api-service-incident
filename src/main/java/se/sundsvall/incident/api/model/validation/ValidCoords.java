package se.sundsvall.incident.api.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({
	ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidCoordsValidator.class)
public @interface ValidCoords {

	String message() default "must be valid coordinates e.g 62.4097,17.24024";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
