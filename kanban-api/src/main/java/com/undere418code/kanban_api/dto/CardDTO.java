package com.undere418code.kanban_api.dto;

import java.time.LocalDate;

import com.undere418code.kanban_api.enums.CardStatus;

public record CardDTO(
        Long id,
        Integer position,
        String title,
        String description,
        Long listId,
        CardStatus status,
        LocalDate dueDate
) {}
