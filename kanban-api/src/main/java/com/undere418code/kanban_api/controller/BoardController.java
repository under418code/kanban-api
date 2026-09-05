package com.undere418code.kanban_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.undere418code.kanban_api.dto.BoardDTO;
import com.undere418code.kanban_api.dto.CreateBoardDTO;
import com.undere418code.kanban_api.dto.UpdateBoardDTO;
import com.undere418code.kanban_api.service.BoardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /** Crée un nouveau board. */
    @PostMapping
    public BoardDTO createBoard(@Valid @RequestBody CreateBoardDTO dto) {
        return boardService.createBoard(dto);
    }

    /** Récupère un board par ID. */
    @GetMapping("/{id}")
    public BoardDTO getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    /** Retourne tous les boards. */
    @GetMapping
    public List<BoardDTO> getAllBoards() {
        return boardService.getAllBoards();
    }

    /** Met à jour un board (PATCH). */
    @PatchMapping("/{id}")
    public BoardDTO updateBoard(@PathVariable Long id, @RequestBody UpdateBoardDTO dto) {
        return boardService.updateBoard(id, dto);
    }

    /** Supprime un board. (DELETE)*/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build(); // return 204(indique qu'une requête a réussi, mais que le serveur ne renvoie aucun contenu dans le corps de la réponse);
    }
}
