package com.nick.cards.controllers;

import com.nick.cards.appmodels.MessageResponse;
import com.nick.cards.appmodels.UserAuditModel;
import com.nick.cards.domain.security.jwt.TokenExtractor;
import com.nick.cards.domain.security.models.request.*;
import com.nick.cards.domain.security.models.response.JwtResponse;
import com.nick.cards.domain.security.models.response.TokenRefreshResponse;
import com.nick.cards.domain.security.services.AuthenticationService;
import com.nick.cards.utils.constants.AppConstant;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = AppConstant.BASE_URL + "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Authentication")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  private final TokenExtractor tokenExtractor;

  public AuthenticationController(
      AuthenticationService authenticationService, TokenExtractor tokenExtractor) {
    this.authenticationService = authenticationService;
    this.tokenExtractor = tokenExtractor;
  }

  @PostMapping("/signIn")
  @ApiOperation(
      value = "Sign in userGuid",
      notes = "Returns the Jwt Response with token  for access to the api",
      response = JwtResponse.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Ok", response = JwtResponse.class),
      })
  public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
      throws AuthException {
    return ResponseEntity.ok(authenticationService.authenticateUser(loginRequest));
  }

  @PostMapping("/signUp")
  @ApiOperation(
      value = "Sign up",
      notes = "Create a new userGuid for auth returns MessageResponse model with status of request",
      response = MessageResponse.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Ok", response = MessageResponse.class),
      })
  public ResponseEntity<MessageResponse> registerUser(
      @Valid @RequestBody SignupRequest signUpRequest, HttpServletRequest request) {
    try {
      UserAuditModel userAuditModel = tokenExtractor.extractUserNameFromToken(request);
      signUpRequest.setCreatedBy(userAuditModel.getCreatedBy());
      signUpRequest.setLastModifiedBy(userAuditModel.getLastModifiedBy());
    } catch (JwtException exception) {
      signUpRequest.setCreatedBy("System");
      signUpRequest.setLastModifiedBy("System");
    }

    return authenticationService.registerUser(signUpRequest);
  }

  @PostMapping("/refreshToken")
  @ApiOperation(
      value = "Refresh Token ",
      notes = "Returns TokenRefreshResponse model with token and another refresh token",
      response = TokenRefreshResponse.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Ok", response = TokenRefreshResponse.class),
      })
  public ResponseEntity<TokenRefreshResponse> refreshToken(
      @Valid @RequestBody TokenRefreshRequest request) {
    return authenticationService.refreshToken(request);
  }

  @PostMapping("/forgotPassword")
  @ApiOperation(
      value = "Forgot Password ",
      notes = "Send an email to the email address if it exists with reset password instructions",
      response = MessageResponse.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Ok", response = MessageResponse.class),
      })
  public ResponseEntity<MessageResponse> forgotPassword(
      @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
    return authenticationService.forgotPassword(forgotPasswordRequest);
  }

  @PostMapping("/resetPassword")
  @ApiOperation(
      value = "Reset Password ",
      notes = "Reset userGuid password given a valid token",
      response = MessageResponse.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Ok", response = MessageResponse.class),
      })
  public ResponseEntity<MessageResponse> resetPassword(
      @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
    return authenticationService.resetPassword(resetPasswordRequest);
  }
}
