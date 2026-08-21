package com.undere418code.kanban_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.undere418code.kanban_api.entity.ListEntity;

/**
 * Repository Spring Data JPA pour ListEntity.
 *
 * 👉 Hérite de toutes les méthodes CRUD via JpaRepository.
 * 👉 On ajoute ici une méthode dérivée utile :
 *      - findByBoardId : Spring génère automatiquement la requête SQL
 *        en analysant le nom de la méthode.
 *
 * Exemple :
 *      findByBoardId(3L)
 *      → SELECT * FROM list WHERE board_id = 3;
 */

public interface ListRepository extends JpaRepository<ListEntity, Long>{

    // Récupère toutes les listes appartenant à un board donné
    List<ListEntity> findByBoardId(Long boardId);
    
    // Calcul de la prochaine position 
    int countByBoardId(Long boardId);
}
