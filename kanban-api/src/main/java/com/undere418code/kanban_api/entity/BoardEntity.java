package com.undere418code.kanban_api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un tableau Kanban.
 * C'est une ENTITY JPA → donc un POJO enrichi par des annotations.
 */
@Entity
@Table(name = "board")
public class BoardEntity {

    /**
     * Identifiant unique du board.
     * 
     * @Id → clé primaire
     * @GeneratedValue → auto-incrément (IDENTITY = géré par la DB)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom du board.
     * nullable = false → obligatoire
     * length = 100 → limite SQL
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Description du board.
     * columnDefinition = TEXT → champ long (SQL)
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Date de création.
     * LocalDateTime.now() → valeur par défaut au moment de l'instanciation.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Date de dernière mise à jour.
     * LocalDateTime.now() → valeur par défaut.
     * À mettre à jour dans le service métier.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Relation OneToMany avec ListEntity.
     * mappedBy = "board" → côté inverse de la relation (dans ListEntity)
     * cascade = ALL → persiste / supprime les listes avec le board
     * orphanRemoval = true → supprime les listes orphelines
     */
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListEntity> lists = new ArrayList<>();

    /**
     * Relation OneToMany avec CardEntity.
     * Même logique que pour les listes.
     */
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardEntity> cards = new ArrayList<>();

    // ------------------ GETTERS / SETTERS ------------------

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<ListEntity> getLists() {
        return this.lists;
    }

    public void setLists(List<ListEntity> lists) {
        this.lists = lists;
    }

    public List<CardEntity> getCards() {
        return this.cards;
    }

    public void setCards(List<CardEntity> cards) {
        this.cards = cards;
    }
}
