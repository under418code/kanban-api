package com.undere418code.kanban_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.undere418code.kanban_api.dto.ListDTO;
import com.undere418code.kanban_api.dto.CreateListDTO;
import com.undere418code.kanban_api.dto.UpdateListDTO;
import com.undere418code.kanban_api.service.ListService;

@RestController
@RequestMapping("/lists")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    /** Crée une nouvelle liste dans un board. */
    @PostMapping
    public ListDTO createList(@RequestBody CreateListDTO dto) {
        return listService.createList(dto);
    }

    /** Récupère une liste par ID. */
    @GetMapping("/{id}")
    public ListDTO getList(@PathVariable Long id) {
        return listService.getList(id);
    }

    /** Retourne toutes les listes d'un board. */
    @GetMapping("/board/{boardId}")
    public List<ListDTO> getListsByBoard(@PathVariable Long boardId) {
        return listService.getListsByBoard(boardId);
    }

    /** Met à jour une liste (PATCH). */
    @PatchMapping("/{id}")
    public ListDTO updateList(@PathVariable Long id, @RequestBody UpdateListDTO dto) {
        return listService.updateList(id, dto);
    }

    /** Supprime une liste. */
    @DeleteMapping("/{id}")
    public void deleteList(@PathVariable Long id) {
        listService.deleteList(id);
    }
}
