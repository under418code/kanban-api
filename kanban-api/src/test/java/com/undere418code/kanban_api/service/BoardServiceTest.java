package com.undere418code.kanban_api.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import com.undere418code.kanban_api.dto.BoardDTO;
import com.undere418code.kanban_api.dto.CreateBoardDTO;
import com.undere418code.kanban_api.dto.UpdateBoardDTO;
import com.undere418code.kanban_api.entity.BoardEntity;
import com.undere418code.kanban_api.exception.NotFoundException;
import com.undere418code.kanban_api.mapper.BoardMapper;
import com.undere418code.kanban_api.repository.BoardRepository;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardMapper boardMapper;

    @InjectMocks
    private BoardService boardService;

    @Test
    void shouldCreateBoard() {
        CreateBoardDTO dto = new CreateBoardDTO("Board A", "Desc");

        BoardEntity saved = new BoardEntity();
        saved.setId(1L);
        saved.setName("Board A");
        saved.setDescription("Desc");

        when(boardRepository.save(any())).thenReturn(saved);
        when(boardMapper.toDTO(saved)).thenReturn(new BoardDTO(1L, "Board A", "Desc"));

        BoardDTO result = boardService.createBoard(dto);

        assertThat(result.id()).isEqualTo(1L);
        verify(boardRepository).save(any());
    }

    @Test
    void shouldThrowWhenBoardNotFound() {
        when(boardRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardService.getBoard(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldUpdateBoard() {
        BoardEntity board = new BoardEntity();
        board.setId(1L);
        board.setName("Old");
        board.setDescription("Test"); // ← obligatoire
        
        UpdateBoardDTO dto = new UpdateBoardDTO(JsonNullable.of("New"), JsonNullable.undefined());

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(boardRepository.save(board)).thenReturn(board);
        when(boardMapper.toDTO(board)).thenReturn(new BoardDTO(1L, "New", "Test"));

        BoardDTO result = boardService.updateBoard(1L, dto);

        assertThat(result.name()).isEqualTo("New");
        verify(boardRepository).save(board);
    }
}
