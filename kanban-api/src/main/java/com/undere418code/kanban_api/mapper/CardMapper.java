package com.undere418code.kanban_api.mapper;

import com.undere418code.kanban_api.dto.CardDTO;
import com.undere418code.kanban_api.entity.CardEntity;

/**
 * Mapper manuel Card ↔ CardDTO.
 * listId est exposé pour éviter de renvoyer l'entité List complète.
 */
public class CardMapper {

    public static CardDTO toDTO(CardEntity card) {
        if (card == null) return null;
        return new CardDTO(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getList().getId(), // on expose seulement l'id
                card.getStatus()
        );
    }

    public static CardEntity toEntity(CardDTO dto) {
        if (dto == null) return null;
        CardEntity card = new CardEntity();
        card.setId(dto.getId());
        card.setTitle(dto.getTitle());
        card.setDescription(dto.getDescription());
        card.setStatus(dto.getStatus());
        // listId sera résolu dans le service (lookup de la List)
        return card;
    }
}
