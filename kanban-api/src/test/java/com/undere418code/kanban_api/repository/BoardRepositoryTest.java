package com.undere418code.kanban_api.repository;

import com.undere418code.kanban_api.entity.BoardEntity;
import com.undere418code.kanban_api.entity.ListEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ListRepository listRepository;

    @Test
    void shouldSaveBoard() {
        BoardEntity board = new BoardEntity();
        board.setName("My Board");
        board.setDescription("Test description");

        BoardEntity saved = boardRepository.save(board);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("My Board");
        assertThat(saved.getDescription()).isEqualTo("Test description");
    }

    @Test
    void shouldFindBoardById() {
        BoardEntity board = new BoardEntity();
        board.setName("Board X");
        board.setDescription("Desc");

        BoardEntity saved = boardRepository.save(board);

        BoardEntity found = boardRepository.findById(saved.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Board X");
    }

    @Test
    void shouldReturnAllBoards() {
        BoardEntity b1 = new BoardEntity();
        b1.setName("A");
        BoardEntity b2 = new BoardEntity();
        b2.setName("B");

        boardRepository.save(b1);
        boardRepository.save(b2);

        List<BoardEntity> all = boardRepository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void shouldUpdateBoard() {
        BoardEntity board = new BoardEntity();
        board.setName("Old");
        board.setDescription("Old desc");

        BoardEntity saved = boardRepository.save(board);

        saved.setName("New");
        saved.setDescription("New desc");

        BoardEntity updated = boardRepository.save(saved);

        assertThat(updated.getName()).isEqualTo("New");
        assertThat(updated.getDescription()).isEqualTo("New desc");
    }

    @Test
    void shouldDeleteBoard() {
        BoardEntity board = new BoardEntity();
        board.setName("To delete");

        BoardEntity saved = boardRepository.save(board);

        boardRepository.delete(saved);

        assertThat(boardRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void deletingBoardShouldDeleteListsBecauseCascadeAndOrphanRemoval() {
        BoardEntity board = new BoardEntity();
        board.setName("Parent");

        ListEntity list = new ListEntity();
        list.setName("Child");
        list.setBoard(board);

        board.getLists().add(list);

        BoardEntity savedBoard = boardRepository.save(board);

        assertThat(listRepository.findAll()).hasSize(1);

        boardRepository.delete(savedBoard);

        assertThat(listRepository.findAll()).isEmpty();
    }
}
