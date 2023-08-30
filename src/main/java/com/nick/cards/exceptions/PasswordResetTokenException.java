package com.nick.cards.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordResetTokenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PasswordResetTokenException(String token, String message) {
        super(String.format("Failed for token [%s]: %s", token, message));
    }
}