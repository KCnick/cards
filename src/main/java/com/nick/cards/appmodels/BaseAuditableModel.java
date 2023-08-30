package com.nick.cards.appmodels;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@ApiModel(value = "BaseAuditableModel", description = "Extensible auditable model")
public abstract class BaseAuditableModel {
    @ApiModelProperty(notes = "date entry was created (Time Stamp) ")
    protected Date dateTimeCreated;

    @ApiModelProperty(notes = "date entry was last modified (Time Stamp) ")
    protected Date lastModifiedDate;
}