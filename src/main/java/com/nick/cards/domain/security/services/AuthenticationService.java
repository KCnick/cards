package com.nick.cards.domain.security.services;

import com.nick.cards.appmodels.MessageResponse;
import com.nick.cards.domain.security.jwt.JwtUtils;
import com.nick.cards.domain.security.models.request.*;
import com.nick.cards.domain.security.models.response.JwtResponse;
import com.nick.cards.domain.security.models.response.TokenRefreshResponse;
import com.nick.cards.domain.security.models.response.UserModel;
import com.nick.cards.domain.security.repositories.RefreshToken;
import com.nick.cards.domain.security.repositories.RoleRepository;
import com.nick.cards.domain.security.repositories.UserRepository;
import com.nick.cards.entities.PasswordResetToken;
import com.nick.cards.entities.Role;
import com.nick.cards.entities.User;
import com.nick.cards.enums.ERole;
import com.nick.cards.exceptions.PasswordResetTokenException;
import com.nick.cards.exceptions.TokenRefreshException;
import com.nick.cards.generators.RandomPasswordGenerator;
import com.nick.cards.utils.MappingUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.security.auth.message.AuthException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class AuthenticationService {

  private static final String ROLE_NOT_FOUND = "Error: Role [%s] is not found.";
  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private UserRepository userRepository;

  @Autowired private RoleRepository roleRepository;

  @Autowired private PasswordEncoder encoder;

  @Autowired private JwtUtils jwtUtils;

  @Autowired private RefreshTokenService refreshTokenService;

  @Autowired private UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired private PasswordResetService passwordResetService;

  @Autowired private ModelMapper modelMapper;

  public JwtResponse authenticateUser(LoginRequest loginRequest) throws AuthException {
    try {

      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginRequest.getUsername(), loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      List<String> roles =
          userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

      return JwtResponse.builder()
          .accessToken(jwt)
          .uuid(userDetails.getUuid())
          .refreshToken(refreshToken.getToken())
          .username(userDetails.getUsername())
          .email(userDetails.getEmail())
          .roles(roles)
          .build();

    } catch (Exception e) {
      log.error(
          "Error authenticating userGuid with model: {} :: Error: {}",
          MappingUtil.mapToJsonStr(loginRequest),
          e.getMessage());
      throw new AuthException(
          String.format(
              "Failed to authenticate username: %s | and error occurred.",
              loginRequest.getUsername()));
    }
  }

  public ResponseEntity<MessageResponse> registerUser(@Valid SignupRequest signUpRequest) {

    log.trace("Register userGuid request: {}", MappingUtil.mapToJsonStr(signUpRequest));

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest()
          .body(
              MessageResponse.builder()
                  .body(signUpRequest)
                  .message("Error: Email is already in use!")
                  .build());
    }

    // Create new userGuid's account
    User user = new User();
    user.setEmail(signUpRequest.getEmail());
    user.setPassword(
        encoder.encode(
            signUpRequest.getPassword() != null
                ? signUpRequest.getPassword()
                : RandomPasswordGenerator.generatePassword()));
    user.setRoles(processRolesToCreate(signUpRequest.getRole()));
    user.setCreatedBy(signUpRequest.getCreatedBy());
    user.setLastModifiedBy(signUpRequest.getLastModifiedBy());
    user.setActive(true);

    user = userRepository.saveAndFlush(user);

    log.trace("userGuid saved  userGuid guid: {}, creating userGuid setting", user.getGuid());

    log.trace("User created successfully {}", user.getGuid());
    return ResponseEntity.ok(
        MessageResponse.builder()
            .body(modelMapper.map(user, UserModel.class))
            .message("User registered successfully!")
            .build());
  }

  public Set<Role> processRolesToCreate(Set<String> strRoles) {

    Set<Role> roles = new HashSet<>();

    if (strRoles == null || strRoles.isEmpty()) {
      Role userRole =
          roleRepository
              .findByName(ERole.ROLE_MEMBER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else strRoles.forEach(role -> addRolesPresent(role, roles));

    return roles;
  }

  private void addRolesPresent(String role, Set<Role> roles) {
    switch (role) {
      case "admin", "ROLE_ADMIN" -> {
        Role adminRole =
            roleRepository
                .findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException(String.format(ROLE_NOT_FOUND, role)));
        roles.add(adminRole);
      }
      default -> {
        Role userRole =
            roleRepository
                .findByName(ERole.ROLE_MEMBER)
                .orElseThrow(() -> new RuntimeException(String.format(ROLE_NOT_FOUND, role)));
        roles.add(userRole);
      }
    }
  }

  public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService
        .findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(
            user -> {
              String token = jwtUtils.generateTokenFromUsername(user.getEmail());
              return ResponseEntity.ok(
                  TokenRefreshResponse.builder()
                      .accessToken(token)
                      .refreshToken(requestRefreshToken)
                      .build());
            })
        .orElseThrow(
            () ->
                new TokenRefreshException(
                    requestRefreshToken, "Refresh token is not in database!"));
  }

  public ResponseEntity<MessageResponse> forgotPassword(
      @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {

    Optional<User> user =
        userDetailsServiceImpl.loadUserByEmail(forgotPasswordRequest.getEmailAddress());
    user.ifPresent(value -> passwordResetService.createPasswordResetTokenForUser(value));
    return ResponseEntity.ok(
        MessageResponse.builder().message("Password reset link sent!").build());
  }

  public ResponseEntity<MessageResponse> resetPassword(
      @Valid ResetPasswordRequest resetPasswordRequest) {
    String resetPasswordToken = resetPasswordRequest.getToken();

    return passwordResetService
        .findByToken(resetPasswordToken)
        .map(passwordResetService::verifyExpiration)
        .map(PasswordResetToken::getUser)
        .map(
            user -> {
              user.setPassword(encoder.encode(resetPasswordRequest.getPassword()));
              userRepository.save(user);
              return ResponseEntity.ok(MessageResponse.builder().message("Password Reset").build());
            })
        .orElseThrow(() -> new PasswordResetTokenException(resetPasswordToken, "token not found"));
  }
}
