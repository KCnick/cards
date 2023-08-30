package com.nick.cards.appmodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import static com.nick.cards.utils.constants.AppConstant.JSON_BODY;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "MessageResponse", description = "Response message model to requests done")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
    @ApiModelProperty(position = 1, example = "created successfully", dataType = "String", notes = "response message")
    private String message;
    @ApiModelProperty(position = 2, example = JSON_BODY, notes = "Any object in question to be modified or created")
    private Object body;

    @ApiModelProperty(position = 3, example = "true", notes = "processing status")
    private boolean success = true;

    public MessageResponse(String transactionRequested) {
        message = transactionRequested;
    }
}