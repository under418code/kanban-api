package com.undere418code.kanban_api.dto;

import java.time.LocalDate;

import org.openapitools.jackson.nullable.JsonNullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.undere418code.kanban_api.enums.CardStatus;

public record UpdateCardDTO(
        // @JsonInclude(JsonInclude.Include.NON_EMPTY)
        JsonNullable<String> title,
        // @JsonInclude(JsonInclude.Include.NON_EMPTY)
        JsonNullable<Integer> position,
        // @JsonInclude(JsonInclude.Include.NON_EMPTY)
        JsonNullable<String> description,
        // @JsonInclude(JsonInclude.Include.NON_EMPTY)
        JsonNullable<CardStatus> status,
        // @JsonInclude(JsonInclude.Include.NON_EMPTY)
        JsonNullable<LocalDate> dueDate
) {}
