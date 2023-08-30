package com.nick.cards.validators;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ColorFormatValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColorFormat {
    String message() default "Invalid color format. It should be in the format #RRGGBB";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
