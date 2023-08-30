package com.nick.cards.exceptions;


import com.nick.cards.exceptions.models.GeneralErrorResponse;
import com.nick.cards.exceptions.models.SpecificErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RestController
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestControllerAdvisor {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralErrorResponse genericException(Exception e, HttpServletRequest request) {
        log.debug("{} error: {}", e.getClass().getSimpleName(), e.getMessage());
        return GeneralErrorResponse.builder()
                .path(request.getRequestURI())
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(e.getClass().getSimpleName())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public GeneralErrorResponse notFoundException(Exception e, HttpServletRequest request) {
        log.debug("Not found error {}", e.getMessage());
        return GeneralErrorResponse.builder()
                .path(request.getRequestURI())
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .error(e.getClass().getSimpleName())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.debug("Invalid input {}", e.getMessage());
        List<SpecificErrorResponse> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new SpecificErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();

        return GeneralErrorResponse.builder()
                .path(request.getRequestURI())
                .message(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(e.getClass().getSimpleName())
                .errors(fieldErrors)
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public GeneralErrorResponse illegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.debug("Invalid argument exception {}", e.getMessage());
        return GeneralErrorResponse.builder()
                .path(request.getRequestURI())
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .error(e.getClass().getSimpleName())
                .build();
    }


    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public GeneralErrorResponse handleTokenRefreshException(TokenRefreshException e, HttpServletRequest request) {
        log.debug("Invalid token refresh {}", e.getMessage());
        return GeneralErrorResponse.builder()
                .path(request.getRequestURI())
                .message(e.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .error(e.getClass().getSimpleName())
                .build();
    }

    @ExceptionHandler(value = PasswordResetTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralErrorResponse handlePasswordResetTokenException(PasswordResetTokenException e, HttpServletRequest request) {
        log.debug("Invalid Password Refresh token {}", e.getMessage());
        return GeneralErrorResponse.builder()
                .path(request.getRequestURI())
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(e.getClass().getSimpleName())
                .build();
    }

    @ExceptionHandler(value = AuthException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public GeneralErrorResponse handleAuthenticationException(AuthException e, HttpServletRequest request) {
        log.debug("Authentication Error {}", e.getMessage());
        return GeneralErrorResponse.builder()
                .path(request.getRequestURI())
                .message(e.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .error(e.getClass().getSimpleName())
                .build();
    }
}
