package com.undere418code.kanban_api.service;

import java.util.List;

import com.undere418code.kanban_api.dto.BoardDTO;
import com.undere418code.kanban_api.dto.CreateBoardDTO;
import com.undere418code.kanban_api.dto.UpdateBoardDTO;

public interface BoardService {
    BoardDTO createBoard(CreateBoardDTO dto);
    BoardDTO getBoard(Long id);
    List<BoardDTO> getAllBoards();
    BoardDTO updateBoard(Long id, UpdateBoardDTO dto);
    void deleteBoard(Long id);
}
