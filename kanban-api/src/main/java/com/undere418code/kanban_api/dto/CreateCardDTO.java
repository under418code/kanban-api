package com.undere418code.kanban_api.dto;

public record CreateCardDTO(
        String title,
        String description,
        Long listId
) {}
