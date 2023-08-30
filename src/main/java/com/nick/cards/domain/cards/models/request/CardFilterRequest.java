package com.nick.cards.domain.cards.models.request;

import com.nick.cards.enums.ECardStatus;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ApiModel(value = "CardFilterRequest", description = "Model to facilitate card filter")
public class CardFilterRequest {
    private String name;
    private String color;
    private String status;
    private LocalDate creationDate;
    private int page;
    private int size;
    private String sortBy;
}
