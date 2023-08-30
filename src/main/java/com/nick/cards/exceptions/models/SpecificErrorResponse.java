package com.nick.cards.exceptions.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecificErrorResponse {
    private String field;
    private String message;
}
