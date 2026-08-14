package com.undere418code.kanban_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.undere418code.kanban_api.dto.BoardDTO;
import com.undere418code.kanban_api.dto.CreateBoardDTO;
import com.undere418code.kanban_api.dto.UpdateBoardDTO;
import com.undere418code.kanban_api.entity.BoardEntity;
import com.undere418code.kanban_api.exception.NotFoundException;
import com.undere418code.kanban_api.mapper.BoardMapper;
import com.undere418code.kanban_api.repository.BoardRepository;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public BoardService(BoardRepository boardRepository, BoardMapper boardMapper) {
        this.boardRepository = boardRepository;
        this.boardMapper = boardMapper;
    }
 /** Crée un nouveau board à partir du DTO d'entrée. */
    public BoardDTO createBoard(CreateBoardDTO dto) {
        BoardEntity board = new BoardEntity();
        board.setName(dto.name());
        board.setDescription(dto.description());

        BoardEntity saved = boardRepository.save(board);
        return boardMapper.toDTO(saved);
    }

     /** Récupère un board par son ID ou lance une NotFoundException. */
    public BoardDTO getBoard(Long id) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Board " + id + " not found"));
        return boardMapper.toDTO(board);
    }

    /** Retourne tous les boards existants. */
    public List<BoardDTO> getAllBoards() {
        return boardRepository.findAll()
                .stream()
                .map(boardMapper::toDTO)
                .toList();
    }

     /** Met à jour un board existant (PATCH). */
    public BoardDTO updateBoard(Long id, UpdateBoardDTO dto) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Board " + id + " not found"));

        if (dto.name() != null) board.setName(dto.name());
        if (dto.description() != null) board.setDescription(dto.description());

        return boardMapper.toDTO(boardRepository.save(board));
    }

     /** Supprime un board si il existe, sinon NotFoundException. */
    public void deleteBoard(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new NotFoundException("Board " + id + " not found");
        }
        boardRepository.deleteById(id);
    }
}
