package com.nick.cards.domain.security.annotations;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Optional;

public class PasswordConstraintsValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        PasswordValidator passwordValidator = new PasswordValidator(
                Arrays.asList(
                        //Length rule. Min 10 max 128 characters
                        new LengthRule(10, 100),

                        //At least one upper case letter
                        new CharacterRule(EnglishCharacterData.UpperCase, 1),

                        //At least one lower case letter
                        new CharacterRule(EnglishCharacterData.LowerCase, 1),

                        //At least one number
                        new CharacterRule(EnglishCharacterData.Digit, 1),

                        //At least one special characters
                        new CharacterRule(EnglishCharacterData.Special, 1),

                        new WhitespaceRule()
                )
        );

        RuleResult result = passwordValidator.validate(new PasswordData(password));

        if (result.isValid())
            return true;

        //Sending one message each time failed validation.
        Optional<String> optionalErrorMessage = passwordValidator.getMessages(result).stream().findFirst();
        optionalErrorMessage.ifPresent(s -> constraintValidatorContext.buildConstraintViolationWithTemplate(s)
                .addConstraintViolation()
                .disableDefaultConstraintViolation());
        return false;
    }

}
