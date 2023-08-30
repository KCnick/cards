package com.nick.cards.domain.security.services;

import com.nick.cards.domain.security.repositories.RefreshToken;
import com.nick.cards.domain.security.repositories.RefreshTokenRepository;
import com.nick.cards.domain.security.repositories.UserRepository;
import com.nick.cards.entities.User;
import com.nick.cards.exceptions.TokenRefreshException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

  @Value("${card.app.jwtRefreshExpirationMs:86400000}")
  private Long refreshTokenDurationMs;

  @Autowired private RefreshTokenRepository refreshTokenRepository;

  @Autowired private UserRepository userRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(Long userId) throws AuthException {
    RefreshToken refreshToken = new RefreshToken();

    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isPresent()) {
      refreshToken.setUser(optionalUser.get());
      refreshToken.setExpiryDate(
          Instant.now()
              .plusMillis(refreshTokenDurationMs != null ? refreshTokenDurationMs : 86400000));
      refreshToken.setToken(UUID.randomUUID().toString());

      refreshToken = refreshTokenRepository.save(refreshToken);
      return refreshToken;
    } else
      throw new AuthException(
          String.format(
              "Failed to create refresh token userGuid not found for id: %s | and error occurred.",
              userId));
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(
          token.getToken(), "Refresh token was expired. Please make a new sign in request");
    }

    return token;
  }
}
