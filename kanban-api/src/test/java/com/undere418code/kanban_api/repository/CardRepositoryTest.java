package com.undere418code.kanban_api.repository;

import com.undere418code.kanban_api.entity.BoardEntity;
import com.undere418code.kanban_api.entity.ListEntity;
import com.undere418code.kanban_api.entity.CardEntity;
import com.undere418code.kanban_api.enums.CardStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ListRepository listRepository;

    @Autowired
    private BoardRepository boardRepository;

    private ListEntity createList(String boardName, String listName) {
        BoardEntity board = new BoardEntity();
        board.setName(boardName);
        board.setDescription("Desc");
        BoardEntity savedBoard = boardRepository.save(board);

        ListEntity list = new ListEntity();
        list.setName(listName);
        list.setBoard(savedBoard);

        return listRepository.save(list);
    }

    @Test
    void shouldSaveCard() {
        ListEntity list = createList("Board A", "To Do");

        CardEntity card = new CardEntity();
        card.setTitle("My Card");
        card.setDescription("Test description");
        card.setPosition(0);
        card.setStatus(CardStatus.ACTIVE);
        card.setList(list);

        CardEntity saved = cardRepository.save(card);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("My Card");
        assertThat(saved.getStatus()).isEqualTo(CardStatus.ACTIVE);
        assertThat(saved.getList().getId()).isEqualTo(list.getId());
    }

    @Test
    void shouldFindCardById() {
        ListEntity list = createList("Board B", "In Progress");

        CardEntity card = new CardEntity();
        card.setTitle("Card X");
        card.setPosition(1);
        card.setList(list);

        CardEntity saved = cardRepository.save(card);

        CardEntity found = cardRepository.findById(saved.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Card X");
    }

    @Test
    void shouldReturnAllCards() {
        ListEntity list = createList("Board C", "List C");

        CardEntity c1 = new CardEntity();
        c1.setTitle("A");
        c1.setPosition(0);
        c1.setList(list);

        CardEntity c2 = new CardEntity();
        c2.setTitle("B");
        c2.setPosition(1);
        c2.setList(list);

        cardRepository.save(c1);
        cardRepository.save(c2);

        List<CardEntity> all = cardRepository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void shouldUpdateCard() {
        ListEntity list = createList("Board D", "List D");

        CardEntity card = new CardEntity();
        card.setTitle("Old");
        card.setPosition(0);
        card.setList(list);

        CardEntity saved = cardRepository.save(card);

        saved.setTitle("New");
        saved.setStatus(CardStatus.ARCHIVED);
        saved.setDueDate(LocalDate.now().plusDays(3));

        CardEntity updated = cardRepository.save(saved);

        assertThat(updated.getTitle()).isEqualTo("New");
        assertThat(updated.getStatus()).isEqualTo(CardStatus.ARCHIVED);
        assertThat(updated.getDueDate()).isNotNull();
    }

    @Test
    void shouldDeleteCard() {
        ListEntity list = createList("Board E", "List E");

        CardEntity card = new CardEntity();
        card.setTitle("To delete");
        card.setPosition(0);
        card.setList(list);

        CardEntity saved = cardRepository.save(card);

        cardRepository.delete(saved);

        assertThat(cardRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void shouldEnforceUniquePositionPerList() {
        ListEntity list = createList("Board F", "List F");

        CardEntity c1 = new CardEntity();
        c1.setTitle("A");
        c1.setPosition(0);
        c1.setList(list);
        cardRepository.save(c1);

        CardEntity c2 = new CardEntity();
        c2.setTitle("B");
        c2.setPosition(0); // même position → doit violer la contrainte
        c2.setList(list);

        assertThatThrownBy(() -> cardRepository.saveAndFlush(c2))
                .isInstanceOf(Exception.class);
    }
}
