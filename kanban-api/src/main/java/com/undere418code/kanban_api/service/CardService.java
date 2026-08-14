package com.undere418code.kanban_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.undere418code.kanban_api.entity.CardEntity;
import com.undere418code.kanban_api.exception.NotFoundException;
import com.undere418code.kanban_api.repository.CardRepository;

/**
 * Service métier pour la gestion des cartes.
 *
 * 👉 Gère les opérations liées aux CardEntity.
 * 👉 Lance NotFoundException si une carte n'existe pas.
 */
@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    /**
    * Récupère toutes les cartes d'une liste.
    */
    
    public List<CardEntity> getCardsByList(Long longId){
        return cardRepository.findByListId(longId);
    }

    /**
    * Crée une nouvelle carte.
    */
    public CardEntity createCard(CardEntity card){
        return cardRepository.save(card);
    }

    /**
     * Récupère une carte par ID.
     *
     * 👉 Si la carte n'existe pas, NotFoundException est lancée.
     * 👉 Le GlobalExceptionHandler renverra un HTTP 404 propre.
     */

    public CardEntity getCardById(Long id){
        return cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Card" + id + " introuvable"));
    }
}
