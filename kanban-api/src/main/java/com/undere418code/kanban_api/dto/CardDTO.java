package com.undere418code.kanban_api.dto;

import com.undere418code.kanban_api.enums.CardStatus;

public record CardDTO(
        Long id,
        String title,
        String description,
        Long listId,
        CardStatus status
) {}
