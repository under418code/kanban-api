package com.undere418code.kanban_api.dto;

/**
 * DTO représentant une List Kanban.
 * boardId permet de lier la liste à son board sans exposer l'entité.
 */
public class ListDTO {
    private Long id;
    private String name;
    private Long boardId;

    public ListDTO() {}

    public ListDTO(Long id, String name, Long boardId) {
        this.id = id;
        this.name = name;
        this.boardId = boardId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getBoardId() {
        return boardId;
    }
}
