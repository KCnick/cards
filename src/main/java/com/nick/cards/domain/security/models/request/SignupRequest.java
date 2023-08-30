package com.nick.cards.domain.security.models.request;

import com.nick.cards.appmodels.UserAuditModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Builder
@ApiModel(value = "SignupRequest", description = "Model to submit userGuid registration details")
public class SignupRequest extends UserAuditModel {
    @NotBlank(message = "please provide a valid email!!")
    @Email
    @Size(max = 50)
    @ApiModelProperty(position = 2, example = "example@card.com", required = true, dataType = "String", notes = "Use your preferred email here")
    private String email;

    @Singular("role")
    @ApiModelProperty(position = 4, required = true, example = "[admin, member]", notes = "Give a list of one role to the userGuid e.g admin, member, empty role defaults to member")
    private Set<String> role;

    @ApiModelProperty(position = 3, example = "card@123", dataType = "String", notes = "Use your preferred password here")
    private String password;

}
