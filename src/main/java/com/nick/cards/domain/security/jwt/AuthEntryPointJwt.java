package com.nick.cards.domain.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nick.cards.exceptions.models.GeneralErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {
        log.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        new ObjectMapper().writeValue(response.getOutputStream(), GeneralErrorResponse.builder()
                .error("Unauthorized")
                .path(request.getServletPath())
                .message(authException.getMessage())
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .build());
    }

}