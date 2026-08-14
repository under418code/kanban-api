package com.undere418code.kanban_api.dto;

/** Payload utilisé pour créer une List dans un Board. */
public class CreateListDTO {
    private String name;
    private Long boardId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getBoardId() { return boardId; }
    public void setBoardId(Long boardId) { this.boardId = boardId; }
}
