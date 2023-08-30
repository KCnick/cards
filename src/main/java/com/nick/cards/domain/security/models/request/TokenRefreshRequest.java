package com.nick.cards.domain.security.models.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "TokenRefreshRequest", description = "Refresh token submission model")
public class TokenRefreshRequest {
    @NotBlank(message = "please provide a valid refreshToken!!")
    @ApiModelProperty(position = 1, example = "a7de36a7-063f-47a8-b6b3-eb2701c66516", required = true, dataType = "String", notes = "Use your stored refreshToken here")
    private String refreshToken;
}