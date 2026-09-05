package com.undere418code.kanban_api.dto;

import java.time.LocalDate;

import com.undere418code.kanban_api.enums.CardStatus;

import jakarta.validation.constraints.*;

public record CreateCardDTO(
        @NotBlank @Size(min = 3, max = 50) String title,
        @NotBlank @Size(min = 3, max = 200) String description,
        @PositiveOrZero Long listId,
        @NotNull CardStatus status,
        @FutureOrPresent LocalDate dueDate
) {}
