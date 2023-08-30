package com.nick.cards.domain.cards.models.response;

import com.nick.cards.appmodels.AuditableModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@ApiModel(value = "CardModel", description = "Extensible auditable model")
public class CardModel extends AuditableModel {

    @ApiModelProperty(position = 1, example = "fd81894a-c996-43c9-b551-451e60555cae", notes = "Card unique identifier")
    private UUID guid;

    @ApiModelProperty(position = 2, example = "My card", notes = "Basic card name. Think of this as the card title")
    private String name;

    @ApiModelProperty(position = 3, example = "#78IU67", notes = "The color of the card. If null, don't worry it's optional")
    private String color;

    @ApiModelProperty(position = 4, example = "Hello there this is my trial card", notes = "The description of the card. If null, don't worry it's optional")
    private String description;
}
