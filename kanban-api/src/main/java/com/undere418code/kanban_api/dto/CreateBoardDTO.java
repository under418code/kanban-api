package com.undere418code.kanban_api.dto;

/** Payload utilisé pour créer un Board. */
public class CreateBoardDTO {
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
