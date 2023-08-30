package com.nick.cards.domain.security.models.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "TokenRefreshResponse", description = "TokenRefreshResponse model to provide userGuid with auth details")
public class TokenRefreshResponse {

    @ApiModelProperty(position = 1, example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3Njk2NDg3MywiZXhwIjoxNjc2OTY4NDczfQ.egANszt7D1KqJxEVXnMiF_N_IgSiJjNhmKdOwcqAnS92-OZgr1rum3DIHIhnc-4bVRrjj6wBPR9y5Ju_N6sH8A", required = true, dataType = "String", notes = "use it for auth to the api")
    private String accessToken;

    @ApiModelProperty(position = 2, example = "a7de36a7-063f-47a8-b6b3-eb2701c66516", required = true, dataType = "String", notes = "use to refresh your access token")
    private String refreshToken;
    @ApiModelProperty(position = 3, example = "Bearer ", required = true, dataType = "String", notes = "Auth type to pass access token")
    @Builder.Default
    private String tokenType = "Bearer";
}