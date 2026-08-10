package com.undere418code.kanban_api.service;

import com.undere418code.kanban_api.repository.ListRepository;
import java.util.List;

import org.springframework.stereotype.Service;

import com.undere418code.kanban_api.entity.ListEntity;
import com.undere418code.kanban_api.exception.NotFoundException;

@Service
public class ListService {

    private final ListRepository listRepository;

    public ListService(ListRepository listReListRepository){
        this.listRepository = listReListRepository;
    }

    /**
    * Récupère toutes les listes d'un board.
    */

    public List<ListEntity> getListById(long boardId){
        return listRepository.findByBoardId(boardId);
    }

    /**
     * Cree une nouvelle liste.
     */

    public ListEntity createList(ListEntity list){
         return listRepository.save(list);
    }

        /**
     * Récupère une liste par ID.
     *
     * 👉 Si la liste n'existe pas, on renvoie une NotFoundException.
     * 👉 Le GlobalExceptionHandler transformera cette exception en HTTP 404.
     */

    public ListEntity geListById(Long id){
        return listRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Liste" + id + " introuvable"));
    }
}
