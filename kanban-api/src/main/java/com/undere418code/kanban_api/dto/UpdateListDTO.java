package com.undere418code.kanban_api.dto;

import org.openapitools.jackson.nullable.JsonNullable;


public record UpdateListDTO(
        // @JsonInclude(JsonInclude.Include.NON_EMPTY) 
        JsonNullable<String> name,
        // @JsonInclude(JsonInclude.Include.NON_EMPTY) 
        JsonNullable<Integer> position
) {}
