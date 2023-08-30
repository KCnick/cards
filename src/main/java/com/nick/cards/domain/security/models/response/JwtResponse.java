package com.nick.cards.domain.security.models.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "JwtResponse", description = "JwtResponse model to provide userGuid with auth details")
public class JwtResponse {
    @ApiModelProperty(position = 1, example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3Njk2NDg3MywiZXhwIjoxNjc2OTY4NDczfQ.egANszt7D1KqJxEVXnMiF_N_IgSiJjNhmKdOwcqAnS92-OZgr1rum3DIHIhnc-4bVRrjj6wBPR9y5Ju_N6sH8A", required = true, dataType = "String", notes = "use it for auth to the api")
    private String accessToken;
    @ApiModelProperty(position = 2, example = "Bearer ", required = true, dataType = "String", notes = "Auth type to pass access token")
    @Builder.Default
    private String type = "Bearer";
    @ApiModelProperty(position = 3, example = "a7de36a7-063f-47a8-b6b3-eb2701c66516", required = true, dataType = "String", notes = "use to refresh your access token")
    private String refreshToken;
    @ApiModelProperty(position = 4, example = "fd81894a-c996-43c9-b551-451e60555cae", required = true, dataType = "String", notes = "userGuid unique uid")
    private UUID uuid;
    @ApiModelProperty(position = 5, example = "example", required = true, dataType = "String", notes = "userGuid name to pass as login parameter")
    private String username;
    @ApiModelProperty(position = 6, example = "example@demo.com", required = true, dataType = "String", notes = "userGuid email address")
    private String email;
    @ApiModelProperty(position = 7, example = "[ROLE_ADMIN,ROLE_USER,ROLE_MODERATOR]", notes = "Roles  belonging to the userGuid")
    private List<String> roles;
}