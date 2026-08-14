package com.undere418code.kanban_api.dto;

import com.undere418code.kanban_api.enums.CardStatus;

public record UpdateCardDTO(
        String title,
        String description,
        CardStatus status
) {}
