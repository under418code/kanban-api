package com.undere418code.kanban_api.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undere418code.kanban_api.dto.BoardDTO;
import com.undere418code.kanban_api.dto.CreateBoardDTO;
import com.undere418code.kanban_api.dto.UpdateBoardDTO;
import com.undere418code.kanban_api.entity.BoardEntity;
import com.undere418code.kanban_api.exception.NotFoundException;
import com.undere418code.kanban_api.mapper.BoardMapper;
import com.undere418code.kanban_api.repository.BoardRepository;
import com.undere418code.kanban_api.util.FieldCheckUtils;
import com.undere418code.kanban_api.util.JsonNullablePatch;

@Service("v1")
@Primary // pas obligatoire ici mais permet d'identifier l'implement a mettre si il existe en existe d'autre
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public BoardServiceImpl(BoardRepository boardRepository, BoardMapper boardMapper) {
        this.boardRepository = boardRepository;
        this.boardMapper = boardMapper;
    }

    /** Crée un nouveau board à partir du DTO d'entrée. */
    @Override
    @Transactional 
    public BoardDTO createBoard(CreateBoardDTO dto) {
        BoardEntity board = boardMapper.toEntity(dto);
        BoardEntity saved = boardRepository.save(board);
        return boardMapper.toDTO(saved);
    }

    /** Récupère un board par son ID ou lance une NotFoundException. */
    @Override
    public BoardDTO getBoard(Long id) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Board " + id + " not found"));
        return boardMapper.toDTO(board);
    }

    /** Retourne tous les boards existants. */
    @Override
    public List<BoardDTO> getAllBoards() {
        return boardRepository.findAll()
                .stream()
                .map(boardMapper::toDTO)
                .toList();
    }

    /** Met à jour un board existant (PATCH). */
    @Override
    @Transactional 
    public BoardDTO updateBoard(Long id, UpdateBoardDTO dto) {

    BoardEntity board = boardRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Board " + id + " not found"));

    dto.name().ifPresent(board::setName);
    // Human readable
    JsonNullablePatch.applyPatch(dto.description(), board::setDescription);
    // Validation métier sur l'entité finale, après application du patch
    FieldCheckUtils.requireNotBlankaAndNotNull(board.getName(),"Board Name");
    FieldCheckUtils.requireNotBlankaAndNotNull(board.getDescription(),"Board Descrition");

    return boardMapper.toDTO(boardRepository.save(board));
    }


     /** Supprime un board si il existe, sinon NotFoundException. */
    @Override 
    @Transactional 
    public void deleteBoard(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new NotFoundException("Board " + id + " not found");
        }
        boardRepository.deleteById(id);
    }
}
