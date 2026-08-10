package com.undere418code.kanban_api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une liste Kanban (ex : "À faire", "En cours", "Terminé").
 * ENTITY JPA → POJO enrichi par des annotations.
 * Chaque liste appartient à un board et contient plusieurs cartes.
 */
@Entity
@Table(name = "list", uniqueConstraints = {
        // Contrainte : une liste ne peut pas avoir la même position dans le même board.
        @UniqueConstraint(name = "uq_list_board_position", columnNames = { "board_id", "position" })
})
public class ListEntity {

    /**
     * Identifiant unique de la liste.
     * 
     * @Id → clé primaire
     * @GeneratedValue → auto-incrément géré par la DB
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Board auquel appartient la liste.
     * 
     * @ManyToOne → plusieurs listes peuvent appartenir à un board.
     *            optional = false → obligatoire
     * @JoinColumn → clé étrangère board_id
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity board;

    /**
     * Nom de la liste (ex : "To Do", "In Progress").
     * nullable = false → obligatoire
     * length = 100 → limite SQL
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Position de la liste dans le board.
     * Utilisée pour l'ordre visuel.
     * Contrainte d'unicité avec board_id.
     */
    @Column(nullable = false)
    private Integer position;

    /**
     * Date de création.
     * LocalDateTime.now() → valeur par défaut.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Date de dernière mise à jour.
     * À mettre à jour dans le service métier.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Relation OneToMany avec CardEntity.
     * mappedBy = "list" → côté inverse de la relation (dans CardEntity)
     * cascade = ALL → persiste / supprime les cartes avec la liste
     * orphanRemoval = true → supprime les cartes orphelines
     */
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardEntity> cards = new ArrayList<>();

    // ------------------ GETTERS / SETTERS ------------------

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BoardEntity getBoard() {
        return this.board;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<CardEntity> getCards() {
        return this.cards;
    }

    public void setCards(List<CardEntity> cards) {
        this.cards = cards;
    }
}
