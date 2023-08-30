package com.nick.cards.domain.security.models.response;

import com.nick.cards.enums.ERole;
import io.swagger.annotations.ApiModel;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "RoleModel", description = "Role details")
public class RoleModel {
    private ERole name;
}