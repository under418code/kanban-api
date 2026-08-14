package com.undere418code.kanban_api.service;

import com.undere418code.kanban_api.entity.BoardEntity;
import com.undere418code.kanban_api.exception.NotFoundException;
import com.undere418code.kanban_api.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier pour la gestion des boards.
 *
 * 👉 Le service contient la logique métier (contrairement au repository).
 * 👉 Il utilise BoardRepository comme adaptateur de persistance.
 * 👉 Cette couche permet de garder les controllers légers et propres.
 * 👉 Elle lance des exceptions métier (NotFoundException) en cas de ressource introuvable.
 */
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    // Injection du repository via le constructeur (bonne pratique)
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * Récupère tous les boards.
     * 👉 Appelle directement le repository (pas de logique métier ici pour l'instant).
     */
    public List<BoardEntity> getAllBoards() {
        return boardRepository.findAll();
    }

    /**
     * Crée un nouveau board.
     * 👉 La logique métier pourra évoluer ici (validation, règles, etc.).
     */
    public BoardEntity createBoard(BoardEntity board) {
        return boardRepository.save(board);
    }

    /**
     * Récupère un board par son ID.
     *
     * 👉 Si le board n'existe pas, on lance NotFoundException.
     * 👉 Le GlobalExceptionHandler transformera cette exception en HTTP 404.
     */
    public BoardEntity getBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new NotFoundException("Board " + id + " introuvable"));
    }
}
