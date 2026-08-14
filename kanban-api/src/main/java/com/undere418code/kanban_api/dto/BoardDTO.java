package com.undere418code.kanban_api.dto;

/**
 * DTO représentant un Board côté API.
 * Utilisé pour exposer les données sans révéler l'entité JPA.
 */
public class BoardDTO {
    private Long id;
    private String name;

    public BoardDTO() {}

    public BoardDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
