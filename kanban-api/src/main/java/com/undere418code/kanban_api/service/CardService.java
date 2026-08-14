package com.undere418code.kanban_api.service;

import java.util.List;

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

    public CardDTO createCard(CreateCardDTO dto) {

        ListEntity list = listRepository.findById(dto.listId())
                .orElseThrow(() -> new NotFoundException("List " + dto.listId() + " not found"));

        CardEntity card = new CardEntity();
        card.setTitle(dto.title());
        card.setDescription(dto.description());
        card.setList(list);

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

        if (dto.title() != null) card.setTitle(dto.title());
        if (dto.description() != null) card.setDescription(dto.description());
        if (dto.status() != null) card.setStatus(dto.status());

        return cardMapper.toDTO(cardRepository.save(card));
    }

    public void deleteCard(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new NotFoundException("Card " + id + " not found");
        }
        cardRepository.deleteById(id);
    }
}
