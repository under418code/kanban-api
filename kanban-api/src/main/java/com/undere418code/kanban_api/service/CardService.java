package com.undere418code.kanban_api.service;

import java.util.List;

import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;

import com.undere418code.kanban_api.dto.CardDTO;
import com.undere418code.kanban_api.dto.CreateCardDTO;
import com.undere418code.kanban_api.dto.UpdateCardDTO;
import com.undere418code.kanban_api.entity.CardEntity;
import com.undere418code.kanban_api.entity.ListEntity;
import com.undere418code.kanban_api.exception.NotFoundException;
import com.undere418code.kanban_api.mapper.CardMapper;
import com.undere418code.kanban_api.repository.CardRepository;
import com.undere418code.kanban_api.repository.ListRepository;
import com.undere418code.kanban_api.util.FieldCheckUtils;
import com.undere418code.kanban_api.util.JsonNullablePatch;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final ListRepository listRepository;
    private final CardMapper cardMapper;

    public CardService(CardRepository cardRepository, ListRepository listRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.listRepository = listRepository;
        this.cardMapper = cardMapper;
    }

    private int nextPosition(Long listId) {
        return cardRepository.countByListId(listId);
    }

    public CardDTO createCard(CreateCardDTO dto) {

        ListEntity list = listRepository.findById(dto.listId())
                .orElseThrow(() -> new NotFoundException("List " + dto.listId() + " not found"));

        CardEntity card = cardMapper.toEntity(dto);
        card.setList(list);
        card.setPosition(nextPosition(dto.listId()));

        return cardMapper.toDTO(cardRepository.save(card));
    }

    public CardDTO getCard(Long id) {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Card " + id + " not found"));
        return cardMapper.toDTO(card);
    }

    public List<CardDTO> getCardsByList(Long listId) {
        return cardRepository.findByListId(listId)
                .stream()
                .map(cardMapper::toDTO)
                .toList();
    }

    public CardDTO updateCard(Long id, UpdateCardDTO dto) {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Card " + id + " not found"));

        boolean applyPatch = JsonNullablePatch.applyPatch(dto.title(), card::setTitle);
        if(applyPatch){FieldCheckUtils.requireNotBlankaAndNotNull(card.getTitle(), "Card Name");}

        applyPatch = JsonNullablePatch.applyPatch(dto.description(), card::setDescription);
        if(applyPatch){FieldCheckUtils.requireNotBlankaAndNotNull(card.getDescription(), "Card Description");}

        applyPatch = JsonNullablePatch.applyPatch(dto.status(), card::setStatus);
        if(applyPatch){FieldCheckUtils.requireNonNull(card.getStatus(), "Card Status");}

        applyPatch = JsonNullablePatch.applyPatch(dto.dueDate(), card::setDueDate);
        if (applyPatch) {FieldCheckUtils.requireNotPast(card.getDueDate(), "Card Date");}

        // Repositionnement
        if (dto.position().isPresent()) {
            int oldPos = card.getPosition();
            int newPos = dto.position().get();
            FieldCheckUtils.requireNotNullandPositive(newPos, "Card Position");

            if (oldPos != newPos) {
                Long listId = card.getList().getId();
                List<CardEntity> cards = cardRepository.findByListId(listId);

                cards.removeIf(c -> c.getId().equals(card.getId()));
                int index = Math.max(0, Math.min(newPos, cards.size()));
                cards.add(index, card);

                // Passe 1 : positions temporaires hors plage (négatives), aucune collision
                // possible
                int tempPos = -1;
                for (CardEntity c : cards) {
                    c.setPosition(tempPos--);
                }
                cardRepository.saveAll(cards);
                cardRepository.flush(); // force le flush avant la 2e passe

                // Passe 2 : positions finales
                int pos = 0;
                for (CardEntity c : cards) {
                    c.setPosition(pos++);
                }
                cardRepository.saveAll(cards);
            }
        }

        return cardMapper.toDTO(cardRepository.save(card));
    }

    public void deleteCard(Long id) {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Card " + id + " not found"));

        Long listId = card.getList().getId();
        int deletedPos = card.getPosition();

        // Supprime la carte
        cardRepository.delete(card);

        // Récupère les cartes restantes
        List<CardEntity> cards = cardRepository.findByListId(listId);

        // Recalcule les positions
        cards.stream()
                .filter(c -> c.getPosition() > deletedPos)
                .forEach(c -> c.setPosition(c.getPosition() - 1));

        cardRepository.saveAll(cards);
    }

}
