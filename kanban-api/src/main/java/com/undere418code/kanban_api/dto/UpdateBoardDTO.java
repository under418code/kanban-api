package com.undere418code.kanban_api.dto;

import org.openapitools.jackson.nullable.JsonNullable;

public record UpdateBoardDTO(
        // @JsonInclude(JsonInclude.Include.NON_EMPTY) 
        JsonNullable<String> name,
        JsonNullable<String> description
) {}
