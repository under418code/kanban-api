package com.undere418code.kanban_api.dto;

/** Payload utilisé pour créer une Card dans une List. */
public class CreateCardDTO {
    private String title;
    private String description;
    private Long listId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getListId() { return listId; }
    public void setListId(Long listId) { this.listId = listId; }
}
