package com.nick.cards.domain.security.services;

import com.nick.cards.domain.security.repositories.PasswordResetRepository;
import com.nick.cards.entities.PasswordResetToken;
import com.nick.cards.entities.User;
import com.nick.cards.exceptions.TokenRefreshException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
  private final PasswordResetRepository passwordResetRepository;

  @Value("${card.app.jwtRefreshExpirationMs:86400000}")
  private Long refreshTokenDurationMs;

  public PasswordResetService(PasswordResetRepository passwordResetRepository) {
    this.passwordResetRepository = passwordResetRepository;
  }

  public PasswordResetToken createPasswordResetTokenForUser(User user) {
    PasswordResetToken passwordResetToken =
        PasswordResetToken.builder()
            .user(user)
            .token(UUID.randomUUID().toString())
            .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
            .build();
    passwordResetToken = passwordResetRepository.save(passwordResetToken);

    return passwordResetToken;
  }

  public Optional<PasswordResetToken> findByToken(String token) {
    return passwordResetRepository.findByToken(token);
  }

  public PasswordResetToken verifyExpiration(PasswordResetToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      throw new TokenRefreshException(token.getToken(), "Password Reset token was expired");
    }

    return token;
  }
}
