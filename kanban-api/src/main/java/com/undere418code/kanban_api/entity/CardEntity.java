package com.undere418code.kanban_api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.undere418code.kanban_api.enums.CardStatus;

/**
 * Représente une carte Kanban.
 * ENTITY JPA → POJO enrichi par des annotations.
 * Contient les informations d'une carte dans une liste d'un board.
 */
@Entity
@Table(name = "card", uniqueConstraints = {
        // Contrainte : une carte ne peut pas avoir la même position dans la même liste.
        @UniqueConstraint(name = "uq_card_list_position", columnNames = { "list_id", "position" })
})
public class CardEntity {

    /**
     * Identifiant unique de la carte.
     * 
     * @Id → clé primaire
     * @GeneratedValue → auto-incrément géré par la DB
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Liste Kanban dans laquelle se trouve la carte.
     * 
     * @ManyToOne → plusieurs cartes dans une même liste.
     *            optional = false → obligatoire
     * @JoinColumn → clé étrangère list_id
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "list_id", nullable = false)
    private ListEntity list;

    /**
     * Titre de la carte.
     * nullable = false → obligatoire
     * length = 200 → limite SQL
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * Description longue de la carte.
     * columnDefinition = TEXT → champ long SQL
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Position de la carte dans la liste.
     * Utilisée pour l'ordre visuel.
     * Contrainte d'unicité avec list_id.
     */
    @Column(nullable = false)
    private Integer position;

    /**
     * Statut de la carte.
     * Ce champ utilise l'ENUM CardStatus, qui définit les différents états possibles
     * d'une carte dans le système (ACTIVE, ARCHIVED, DELETED).
     * Valeur par défaut : ACTIVE.
     * @Enumerated
     * → Indique à JPA de stocker l'ENUM sous forme de texte dans la base de données.
     *   Cela garantit une correspondance directe avec le type PostgreSQL "card_status"
     *   et évite les problèmes liés aux valeurs ordinales.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status = CardStatus.ACTIVE;

    /**
     * Date limite (deadline) de la carte.
     * LocalDate → sans heure.
     */
    private LocalDate dueDate;

    /**
     * Date de création gerer par la bdd.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt ;

    /**
     * Date de dernière mise à jour gerer par la bdd.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt ;

    // ------------------ GETTERS / SETTERS ------------------

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ListEntity getList() {
        return this.list;
    }

    public void setList(ListEntity list) {
        this.list = list;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public CardStatus getStatus() {
        return this.status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
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
}
