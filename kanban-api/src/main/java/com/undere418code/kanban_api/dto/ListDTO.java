package com.undere418code.kanban_api.dto;

public record ListDTO(
        Long id,
        Integer position,
        String name,
        Long boardId
) {}
