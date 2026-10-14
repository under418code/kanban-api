package com.undere418code.kanban_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.undere418code.kanban_api.dto.CardDTO;
import com.undere418code.kanban_api.dto.CreateCardDTO;
import com.undere418code.kanban_api.dto.UpdateCardDTO;
import com.undere418code.kanban_api.enums.CardStatus;
import com.undere418code.kanban_api.service.CardServiceImpl;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objetMapper;

    @MockitoBean
    private CardServiceImpl cardService;

    // ---------------------------------------------------------
    // GET /cards/{id}
    // ---------------------------------------------------------
    @Test
    void getCardById_returnCardDTO() throws Exception{
        CardDTO dto = new CardDTO(
            12L,
            5,
            "excecute task X",
            "essential task",
            3L,
            CardStatus.ACTIVE,
            LocalDate.now()
        );

        when(cardService.getCard(12L)).thenReturn(dto);

        String expectedJson = objetMapper.writeValueAsString(dto);

        mockMvc.perform(get("/cards/12")) // recupere a l'id card 12 
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
        
        verify(cardService).getCard(12L);
    }


    // ---------------------------------------------------------
    // GET /cards/list/{listid}
    // ---------------------------------------------------------
    @Test
    void getAllCardsByOneListId_retrunListOfCardDTO() throws Exception{

            CardDTO dto1 = new CardDTO(
                12L,
                5,
                "execute task X",
                "essential task",
                3L,
                CardStatus.ACTIVE,
                LocalDate.now()
            );

            CardDTO dto2 = new CardDTO(
                13L,
                8,
                "fix bug Y",
                "critical issue",
                3L,
                CardStatus.ARCHIVED,
                LocalDate.now().plusDays(1)
            );

            List<CardDTO> lists = List.of(
                dto1,
                dto2
            );

            when(cardService.getCardsByList(3L)).thenReturn(lists);

            String exceptedString = objetMapper.writeValueAsString(lists);

            mockMvc.perform(get("/cards/list/3"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(exceptedString));
            
            verify(cardService).getCardsByList(3L);
    }
    // ---------------------------------------------------------
    // POST /cards
    // ---------------------------------------------------------
    @Test
    void createCard_returnCreatedCardDTO() throws Exception{
        CreateCardDTO input = new CreateCardDTO("execute task X", "essential task", 3L, CardStatus.ACTIVE, LocalDate.now());
        CardDTO output = new CardDTO(
                12L,
                5,
                "execute task X",
                "essential task",
                3L,
                CardStatus.ACTIVE,
                LocalDate.now()
            );
        
        when(cardService.createCard(any(CreateCardDTO.class))).thenReturn(output);

        mockMvc.perform(post("/cards")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objetMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(content().json(objetMapper.writeValueAsString(output)));
        verify(cardService).createCard(eq(input));
        
    }

    // ---------------------------------------------------------
    // PATCH /cards/{id}
    // ---------------------------------------------------------
    @Test
    void updateCard_returnUpdatedCardDTO() throws Exception{
        UpdateCardDTO input = new UpdateCardDTO(JsonNullable.undefined(), JsonNullable.undefined(), JsonNullable.of("essential task update"), JsonNullable.of(CardStatus.ARCHIVED), JsonNullable.undefined());
        CardDTO output = new CardDTO(6L, 5, "execute task X","essential task update", 32L, CardStatus.ARCHIVED, LocalDate.of(2024, 9, 4));

        when(cardService.updateCard(eq(6L), any(UpdateCardDTO.class))).thenReturn(output);

        mockMvc.perform(patch("/cards/6")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objetMapper.writeValueAsBytes(input)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objetMapper.writeValueAsString(output)));
        
        verify(cardService).updateCard(eq(6L), eq(input));

    }
    // ---------------------------------------------------------
    // DELETE /cards/{id}
    // ---------------------------------------------------------
    @Test
    void deletedCard_noContentReturns() throws Exception{
        doNothing().when(cardService).deleteCard(3L);

        mockMvc.perform(delete("/cards/3"))
                .andExpect(status().isNoContent());

        verify(cardService).deleteCard(3L);
    }
}
