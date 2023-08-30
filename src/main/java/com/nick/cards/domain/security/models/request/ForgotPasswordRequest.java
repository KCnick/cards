package com.nick.cards.domain.security.models.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ResetPasswordRequest", description = "Reset password submission model")
public class ForgotPasswordRequest {
    @NotBlank(message = "please provide a valid email address")
    @ApiModelProperty(position = 1, example = "john.doe@example.com", required = true, dataType = "String", notes = "Email address of the password to reset")
    @Email(message = "please provide a valid email")
    private String emailAddress;
}
