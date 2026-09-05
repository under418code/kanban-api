package com.undere418code.kanban_api.dto;

import jakarta.validation.constraints.*;

public record CreateListDTO(
        @NotBlank @Size(min = 3, max = 50) String name,
        @PositiveOrZero  Long boardId
) {}
