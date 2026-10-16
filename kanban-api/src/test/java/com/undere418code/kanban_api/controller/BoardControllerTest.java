package com.undere418code.kanban_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.undere418code.kanban_api.dto.BoardDTO;
import com.undere418code.kanban_api.dto.CreateBoardDTO;
import com.undere418code.kanban_api.dto.UpdateBoardDTO;
import com.undere418code.kanban_api.service.BoardServiceImpl;

import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired 
    private ObjectMapper objectMapper;

    @MockitoBean(name = "v1")
    private BoardServiceImpl boardService;

    // ---------------------------------------------------------
    // GET /boards/{id}
    // ---------------------------------------------------------
    @Test
    void getBoardById_returnsBoardDTO() throws Exception {

        BoardDTO dto = new BoardDTO(1L, "Board A", "Description A");
        when(boardService.getBoard(1L)).thenReturn(dto);

        mockMvc.perform(get("/boards/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));

        verify(boardService).getBoard(1L);
    }

    // ---------------------------------------------------------
    // GET /boards
    // ---------------------------------------------------------
    @Test
    void getAllBoards_returnsListOfBoardDTO() throws Exception {

        List<BoardDTO> boards = List.of(
                new BoardDTO(1L, "Board A", "Desc A"),
                new BoardDTO(2L, "Board B", "Desc B")
        );

        when(boardService.getAllBoards()).thenReturn(boards);

        mockMvc.perform(get("/boards"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(boards)));

        verify(boardService).getAllBoards();
    }

    // ---------------------------------------------------------
    // POST /boards
    // ---------------------------------------------------------
    @Test
    void createBoard_returnsCreatedBoardDTO() throws Exception {
        CreateBoardDTO input = new CreateBoardDTO("New Board", "New Desc");
        BoardDTO output = new BoardDTO(10L, "New Board", "New Desc");

        when(boardService.createBoard(any(CreateBoardDTO.class))).thenReturn(output);

        mockMvc.perform(post("/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(output)));

        verify(boardService).createBoard(eq(input));
    }

    // ---------------------------------------------------------
    // PATCH /boards/{id}
    // ---------------------------------------------------------
    @Test
    void updateBoard_returnsUpdatedBoardDTO() throws Exception {

        UpdateBoardDTO input = new UpdateBoardDTO(JsonNullable.undefined(), JsonNullable.of("Updated Desc"));
        BoardDTO output = new BoardDTO(1L, "Task", "Updated Desc");

        when(boardService.updateBoard(eq(1L), any(UpdateBoardDTO.class))).thenReturn(output);

        mockMvc.perform(patch("/boards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(output)));

        verify(boardService).updateBoard(eq(1L), eq(input));
    }

    // ---------------------------------------------------------
    // DELETE /boards/{id}
    // ---------------------------------------------------------
    @Test
    void deleteBoard_returnsNoContent() throws Exception {

        doNothing().when(boardService).deleteBoard(1L);

        mockMvc.perform(delete("/boards/1"))
                .andExpect(status().isNoContent());

        verify(boardService).deleteBoard(1L);
    }
}
