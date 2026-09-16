package com.undere418code.kanban_api.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import com.undere418code.kanban_api.dto.ListDTO;
import com.undere418code.kanban_api.dto.CreateListDTO;
import com.undere418code.kanban_api.dto.UpdateListDTO;
import com.undere418code.kanban_api.entity.BoardEntity;
import com.undere418code.kanban_api.entity.ListEntity;
import com.undere418code.kanban_api.exception.NotFoundException;
import com.undere418code.kanban_api.mapper.ListMapper;
import com.undere418code.kanban_api.repository.BoardRepository;
import com.undere418code.kanban_api.repository.ListRepository;

@ExtendWith(MockitoExtension.class)
class ListServiceTest {

    @Mock
    private ListRepository listRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ListMapper listMapper;

    @InjectMocks
    private ListServiceImpl listService;

    @Test
    void shouldCreateList() {
        CreateListDTO dto = new CreateListDTO("Todo", 1L);

        BoardEntity board = new BoardEntity();
        board.setId(1L);

        ListEntity saved = new ListEntity();
        saved.setId(10L);
        when(listMapper.toEntity(any())).thenReturn(new ListEntity());

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(listRepository.countByBoardId(1L)).thenReturn(0);
        when(listRepository.save(any())).thenReturn(saved);
        when(listMapper.toDTO(saved)).thenReturn(new ListDTO(10L, 0, "Todo", 1L));

        ListDTO result = listService.createList(dto);

        assertThat(result.id()).isEqualTo(10L);
        verify(listRepository).save(any());
    }

    @Test
    void shouldThrowWhenListNotFound() {
        when(listRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listService.getList(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldRepositionList() {
        ListEntity list = new ListEntity();
        list.setId(1L);
        list.setPosition(0);

        ListEntity other = new ListEntity();
        other.setId(2L);
        other.setPosition(1);

        BoardEntity board = new BoardEntity();
        board.setId(99L);
        list.setBoard(board);
        other.setBoard(board);

        UpdateListDTO dto = new UpdateListDTO(JsonNullable.of("New"), JsonNullable.of(1));

        when(listRepository.findById(1L)).thenReturn(Optional.of(list));
        when(listRepository.findByBoardId(99L)).thenReturn(new java.util.ArrayList<>(List.of(list, other)));

        listService.updateList(1L, dto);

        verify(listRepository, times(2)).saveAll(any());
    }
}
