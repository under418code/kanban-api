package com.undere418code.kanban_api.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.undere418code.kanban_api.dto.CardDTO;
import com.undere418code.kanban_api.dto.CreateCardDTO;
import com.undere418code.kanban_api.dto.UpdateCardDTO;
import com.undere418code.kanban_api.entity.CardEntity;
import com.undere418code.kanban_api.entity.ListEntity;
import com.undere418code.kanban_api.enums.CardStatus;
import com.undere418code.kanban_api.exception.NotFoundException;
import com.undere418code.kanban_api.mapper.CardMapper;
import com.undere418code.kanban_api.repository.CardRepository;
import com.undere418code.kanban_api.repository.ListRepository;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private ListRepository listRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    @Test
    void shouldCreateCard() {
        CreateCardDTO dto = new CreateCardDTO(
                "Card A",
                "Desc",
                1L,
                CardStatus.ACTIVE,
                LocalDate.of(2026, 9, 1));

        ListEntity list = new ListEntity();
        list.setId(1L);

        CardEntity saved = new CardEntity();
        saved.setId(100L);

        when(listRepository.findById(1L)).thenReturn(Optional.of(list));
        when(cardRepository.countByListId(1L)).thenReturn(0);
        when(cardRepository.save(any())).thenReturn(saved);
        when(cardMapper.toDTO(saved)).thenReturn(
                new CardDTO(100L, 0, "Desc", "Card A", 1L, CardStatus.ACTIVE, LocalDate.of(2026, 9, 1)));

        CardDTO result = cardService.createCard(dto);

        assertThat(result.id()).isEqualTo(100L);
        verify(cardRepository).save(any());
    }

    @Test
    void shouldThrowWhenCardNotFound() {
        when(cardRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardService.getCard(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldRepositionCard() {
        CardEntity card = new CardEntity();
        card.setId(1L);
        card.setPosition(0);

        CardEntity other = new CardEntity();
        other.setId(2L);
        other.setPosition(1);

        ListEntity list = new ListEntity();
        list.setId(99L);
        card.setList(list);
        other.setList(list);

        UpdateCardDTO dto = new UpdateCardDTO(
                null, // title
                1, // position → repositionnement OK
                null, // description
                null, // status
                null // dueDate
        );

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.findByListId(99L)).thenReturn(new java.util.ArrayList<>(List.of(card, other)));

        cardService.updateCard(1L, dto);

        verify(cardRepository, times(2)).saveAll(any());
    }

    @Test
    void shouldDeleteCardAndReposition() {
        CardEntity card = new CardEntity();
        card.setId(1L);
        card.setPosition(0);

        ListEntity list = new ListEntity();
        list.setId(99L);
        card.setList(list);

        CardEntity other = new CardEntity();
        other.setId(2L);
        other.setPosition(1);
        other.setList(list);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.findByListId(99L)).thenReturn(List.of(other));

        cardService.deleteCard(1L);

        verify(cardRepository).delete(card);
        verify(cardRepository).saveAll(any());
    }
}
