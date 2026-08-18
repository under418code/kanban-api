package com.undere418code.kanban_api.dto;

import java.time.LocalDate;

import com.undere418code.kanban_api.enums.CardStatus;

public record UpdateCardDTO(
        String title,
        Integer position,
        String description,
        CardStatus status,
        LocalDate dueDate
) {}
