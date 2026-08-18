package com.undere418code.kanban_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.undere418code.kanban_api.dto.CardDTO;
import com.undere418code.kanban_api.dto.CreateCardDTO;
import com.undere418code.kanban_api.dto.UpdateCardDTO;
import com.undere418code.kanban_api.service.CardService;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /** Crée une carte dans une liste. */
    @PostMapping
    public CardDTO createCard(@RequestBody CreateCardDTO dto) {
        return cardService.createCard(dto);
    }

    /** Récupère une carte par ID. */
    @GetMapping("/{id}")
    public CardDTO getCard(@PathVariable Long id) {
        return cardService.getCard(id);
    }

    /** Retourne toutes les cartes d'une liste. */
    @GetMapping("/list/{listId}")
    public List<CardDTO> getCardsByList(@PathVariable Long listId) {
        return cardService.getCardsByList(listId);
    }

    /** Met à jour une carte (PATCH). */
    @PatchMapping("/{id}")
    public CardDTO updateCard(@PathVariable Long id, @RequestBody UpdateCardDTO dto) {
        return cardService.updateCard(id, dto);
    }

    /** Supprime une carte. */
    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }
}
