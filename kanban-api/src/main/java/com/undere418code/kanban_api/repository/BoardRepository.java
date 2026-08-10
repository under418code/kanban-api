package com.undere418code.kanban_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.undere418code.kanban_api.entity.BoardEntity;

/**
 * Repository Spring Data JPA pour BoardEntity.
 *
 * 👉 C'est une interface (un contrat technique) qui définit l'accès aux données.
 * 👉 Elle étend JpaRepository, ce qui donne automatiquement :
 *      - CRUD complet (save, findById, findAll, deleteById, ...)
 *      - pagination et tri
 *      - gestion des transactions
 *
 * ⚠️ Aucun code à écrire ici : Spring génère l'implémentation automatiquement.
 */

public interface BoardRepository extends JpaRepository<BoardEntity, Long>{
}
