package com.undere418code.kanban_api.dto;

import org.openapitools.jackson.nullable.JsonNullable;

import com.fasterxml.jackson.annotation.JsonInclude;

public record UpdateBoardDTO(
        // @JsonInclude(JsonInclude.Include.NON_EMPTY) 
        JsonNullable<String> name,
        JsonNullable<String> description
) {}
