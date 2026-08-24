package com.undere418code.kanban_api.repository;

import com.undere418code.kanban_api.entity.BoardEntity;
import com.undere418code.kanban_api.entity.ListEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class ListRepositoryTest {

    @Autowired
    private ListRepository listRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void shouldSaveList() {
        BoardEntity board = new BoardEntity();
        board.setName("Board A");
        board.setDescription("Desc");
        BoardEntity savedBoard = boardRepository.save(board);

        ListEntity list = new ListEntity();
        list.setName("To Do");
        list.setPosition(0);
        list.setBoard(savedBoard);

        ListEntity saved = listRepository.save(list);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("To Do");
        assertThat(saved.getPosition()).isEqualTo(0);
        assertThat(saved.getBoard().getId()).isEqualTo(savedBoard.getId());
    }

    @Test
    void shouldFindListById() {
        BoardEntity board = new BoardEntity();
        board.setName("Board B");
        board.setDescription("Desc");
        BoardEntity savedBoard = boardRepository.save(board);

        ListEntity list = new ListEntity();
        list.setName("In Progress");
        list.setPosition(1);
        list.setBoard(savedBoard);

        ListEntity saved = listRepository.save(list);

        ListEntity found = listRepository.findById(saved.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("In Progress");
    }

    @Test
    void shouldReturnAllLists() {
        BoardEntity board = new BoardEntity();
        board.setName("Board C");
        board.setDescription("Desc");
        BoardEntity savedBoard = boardRepository.save(board);

        ListEntity l1 = new ListEntity();
        l1.setName("A");
        l1.setPosition(0);
        l1.setBoard(savedBoard);

        ListEntity l2 = new ListEntity();
        l2.setName("B");
        l2.setPosition(1);
        l2.setBoard(savedBoard);

        listRepository.save(l1);
        listRepository.save(l2);

        List<ListEntity> all = listRepository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void shouldUpdateList() {
        BoardEntity board = new BoardEntity();
        board.setName("Board D");
        board.setDescription("Desc");
        BoardEntity savedBoard = boardRepository.save(board);

        ListEntity list = new ListEntity();
        list.setName("Old");
        list.setPosition(0);
        list.setBoard(savedBoard);

        ListEntity saved = listRepository.save(list);

        saved.setName("New");
        saved.setPosition(5);

        ListEntity updated = listRepository.save(saved);

        assertThat(updated.getName()).isEqualTo("New");
        assertThat(updated.getPosition()).isEqualTo(5);
    }

    @Test
    void shouldDeleteList() {
        BoardEntity board = new BoardEntity();
        board.setName("Board E");
        board.setDescription("Desc");
        BoardEntity savedBoard = boardRepository.save(board);

        ListEntity list = new ListEntity();
        list.setName("To delete");
        list.setPosition(0);
        list.setBoard(savedBoard);

        ListEntity saved = listRepository.save(list);

        listRepository.delete(saved);

        assertThat(listRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void shouldEnforceUniquePositionPerBoard() {
        BoardEntity board = new BoardEntity();
        board.setName("Board F");
        board.setDescription("Desc");
        BoardEntity savedBoard = boardRepository.save(board);

        ListEntity l1 = new ListEntity();
        l1.setName("A");
        l1.setPosition(0);
        l1.setBoard(savedBoard);
        listRepository.save(l1);

        ListEntity l2 = new ListEntity();
        l2.setName("B");
        l2.setPosition(0); // même position → doit violer la contrainte
        l2.setBoard(savedBoard);

        assertThatThrownBy(() -> listRepository.saveAndFlush(l2))
                .isInstanceOf(Exception.class);
    }
}
