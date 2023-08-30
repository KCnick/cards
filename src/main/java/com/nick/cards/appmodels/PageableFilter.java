package com.nick.cards.appmodels;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "PageableFilter", description = "Model to filter data fetch")
public class PageableFilter {

    @ApiModelProperty(position = 1, notes = "If data should be paginated")
    @Builder.Default
    private boolean paged = true;

    @ApiModelProperty(position = 2, example = "0", notes = "The current page number.")
    @Builder.Default
    private Integer pageNumber = 0;
    @ApiModelProperty(position = 3, example = "10", notes = "Number of records on each page.")
    @Builder.Default
    private Integer pageSize = 10;
}