package com.undere418code.kanban_api.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.undere418code.kanban_api.entity.CardEntity;
/**
 * Repository Spring Data JPA pour CardEntity.
 *
 * 👉 Comme les autres repositories, il fournit automatiquement :
 *      - save()
 *      - findById()
 *      - findAll()
 *      - deleteById()
 *
 * 👉 On ajoute une méthode dérivée pour récupérer les cartes d'une liste.
 *      Spring génère la requête SQL automatiquement.
 */
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    // Récupère toutes les cartes appartenant à une liste donnée
    List<CardEntity> findByListId(Long listId);
}
