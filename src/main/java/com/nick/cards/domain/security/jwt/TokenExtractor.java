package com.nick.cards.domain.security.jwt;

import com.nick.cards.appmodels.UserAuditModel;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;

import static com.nick.cards.domain.security.utils.SecurityUtil.parseJwt;

@Service
@Slf4j
public class TokenExtractor {

    private final JwtUtils jwtUtils;

    public TokenExtractor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public UserAuditModel extractUserNameFromToken(HttpServletRequest request) {
        String jwt = parseJwt(request);
        if (jwt != null) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            return new UserAuditModel(username, username);
        }
        throw new JwtException("Error extracting username from token");
    }
}