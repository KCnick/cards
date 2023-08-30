package com.nick.cards.appmodels;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "AuditableModel", description = "Extensible auditable model")
public abstract class AuditableModel extends BaseAuditableModel {

    @ApiModelProperty(notes = "Entry  was created by ( Username for a system User) ", example = "Jane Doe")
    protected String createdBy;

    @ApiModelProperty(notes = "Entry was last modified by  ( Username for a system User)", example = "Jane Doe")
    protected String lastModifiedBy;
}