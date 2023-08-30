package com.nick.cards.domain.security.models.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@ApiModel(value = "LoginRequest", description = "Model to submit login details i.e. username and password ")
public class LoginRequest {

    @NotBlank(message = "please provide a valid username!!")
    @ApiModelProperty(position = 1, example = "myUsername", required = true, dataType = "String", notes = "Use your registered username here")
    private String username;

    @NotBlank(message = "please provide a valid password!!")
    @NotNull
    @ApiModelProperty(position = 2, example = "myPassword", required = true, dataType = "String", notes = "Use your registered password here")
    private String password;
}