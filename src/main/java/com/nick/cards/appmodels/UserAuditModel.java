package com.nick.cards.appmodels;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "UserAuditModel", description = "User audit model for creation and modification")
@NoArgsConstructor
@AllArgsConstructor
public class UserAuditModel {
    @ApiModelProperty(notes = "Entry  was created by ( Username for a system User) ", example = "Jane Doe")
    private String createdBy;
    @ApiModelProperty(notes = "Entry was last modified by  ( Username for a system User)", example = "Jane Doe")
    private String lastModifiedBy;
}