package com.undere418code.kanban_api.dto;

import com.undere418code.kanban_api.enums.CardStatus;

/**
 * DTO représentant une Card Kanban.
 * Contient uniquement les champs nécessaires à l'API.
 */
public class CardDTO {
    private Long id;
    private String title;
    private String description;
    private Long listId;
    private CardStatus status;

    public CardDTO() {}

    public CardDTO(Long id, String title, String description, Long listId, CardStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.listId = listId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getListId() {
        return listId;
    }

    public CardStatus getStatus() {
        return status;
    }
}
