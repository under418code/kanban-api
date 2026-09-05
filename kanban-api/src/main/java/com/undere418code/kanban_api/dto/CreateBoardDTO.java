package com.undere418code.kanban_api.dto;

import jakarta.validation.constraints.*;

public record CreateBoardDTO(
        @NotBlank @Size(min = 3, max = 50) String name,
        @NotBlank @Size(min = 3, max = 200) String description
) {}
