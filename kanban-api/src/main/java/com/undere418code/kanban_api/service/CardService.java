package com.undere418code.kanban_api.service;

import java.util.List;

import com.undere418code.kanban_api.dto.CardDTO;
import com.undere418code.kanban_api.dto.CreateCardDTO;
import com.undere418code.kanban_api.dto.UpdateCardDTO;

public interface CardService {
    CardDTO createCard(CreateCardDTO dto);
    CardDTO getCard(Long id);
    List<CardDTO> getCardsByList(Long id);
    CardDTO updateCard(Long id, UpdateCardDTO dto);
    void deleteCard(Long id);
}
