package com.nick.cards.domain.security.models.response;

import com.nick.cards.appmodels.AuditableModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.usertype.UserType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "UserModel", description = "User details")
public class UserModel extends AuditableModel {

    @ApiModelProperty(position = 1, example = "fd81894a-c996-43c9-b551-451e60555cae", notes = "User unique identifier")
    private UUID guid;

    @ApiModelProperty(position = 3, example = "userGuid@email.com", notes = "userGuid email address")
    private String email;

    @ApiModelProperty(position = 4, example = "[ROLE_ADMIN,ROLE_USER,ROLE_MODERATOR]", notes = "List of userGuid roles")
    @Builder.Default
    private Set<RoleModel> roles = new HashSet<>();


}