package com.nick.cards.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ColorFormatValidator implements ConstraintValidator<ColorFormat, String> {

    @Override
    public void initialize(ColorFormat constraintAnnotation) {
        //No harm having no implementation here.

    }

    @Override
    public boolean isValid(String color, ConstraintValidatorContext context) {
        if (color == null) {
            return true;
        }
        return color.matches("^#[0-9a-fA-F]{6}$");
    }
}
