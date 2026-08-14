package com.undere418code.kanban_api.enums;

/**
 * Représente le statut métier d'une carte Kanban.
 *
 * Cet ENUM définit les différents états possibles d'une carte dans le système.
 * Il est utilisé :
 *  - dans l'entité CardEntity (champ "status")
 *  - dans la base PostgreSQL via le type ENUM "card_status"
 *  - dans les DTO et les services métier pour gérer les transitions de statut
 *
 * Pourquoi un ENUM ?
 *  - garantit des valeurs strictes (pas d'erreur de typo)
 *  - cohérence totale entre Java et PostgreSQL
 *  - facilite les règles métier (archiver, supprimer, restaurer)
 *
 * Valeurs :
 *  ACTIVE   → carte visible et utilisable dans le board
 *  ARCHIVED → carte retirée de la vue principale mais conservée
 *  DELETED  → carte supprimée logiquement (soft delete éventuel)
 */
public enum CardStatus {

    /** Carte active, visible dans la liste */
    ACTIVE,

    /** Carte archivée, non visible mais conservée */
    ARCHIVED,

    /** Carte supprimée (soft delete ou logique métier) */
    DELETED
}
