package com.nick.cards.domain.cards.models.request;

import com.nick.cards.validators.ColorFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ApiModel(value = "CreateCardRequest", description = "Model to submit Card details i.e. name, color, description")

public class CreateCardRequest {

    @NotBlank(message = "Please provide a valid name")
    @ApiModelProperty(position = 2, example = "My Card", required = true, dataType = "String", notes = "Give you card a name. This field is mandatory")
    private String name;

    @ColorFormat
    @ApiModelProperty(position = 3, example = "#RRGGBB", required = false, dataType = "String", notes = "Set a preferred color for your card. This field is not mandatory")
    private String color;

    @ApiModelProperty(position = 4, example = "This card describes the initial configuration of your application", required = false, dataType = "String", notes = "Give general info about your card here. This field is not mandatory")
    private String description;
}
