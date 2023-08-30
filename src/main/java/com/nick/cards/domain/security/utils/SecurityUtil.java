package com.nick.cards.domain.security.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public final class SecurityUtil {

  private SecurityUtil() {}

  public static String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.startsWithIgnoreCase(headerAuth, "Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }
}
