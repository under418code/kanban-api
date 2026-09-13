package com.undere418code.kanban_api.dto;

import org.openapitools.jackson.nullable.JsonNullable;

import com.fasterxml.jackson.annotation.JsonInclude;

public record UpdateListDTO(
        // @JsonInclude(JsonInclude.Include.NON_EMPTY) 
        JsonNullable<String> name,
        // @JsonInclude(JsonInclude.Include.NON_EMPTY) 
        JsonNullable<Integer> position
) {}
