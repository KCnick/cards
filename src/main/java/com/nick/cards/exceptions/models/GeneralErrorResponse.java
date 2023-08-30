package com.nick.cards.exceptions.models;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralErrorResponse {
    private int status;
    @Builder.Default
    private Date timestamp = new Date();
    private String message;
    private String error;
    private String path;
    @Singular("errors")
    private List<SpecificErrorResponse> errors;
}
