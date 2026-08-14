package com.undere418code.kanban_api.mapper;

import com.undere418code.kanban_api.dto.ListDTO;
import com.undere418code.kanban_api.entity.ListEntity;

/**
 * Mapper manuel List ↔ ListDTO.
 * boardId est mappé manuellement pour éviter d'exposer l'entité Board.
 */
public class ListMapper {

    public static ListDTO toDTO(ListEntity list) {
        if (list == null) return null;
        return new ListDTO(
                list.getId(),
                list.getName(),
                list.getBoard().getId() // on expose seulement l'id
        );
    }

    public static ListEntity toEntity(ListDTO dto) {
        if (dto == null) return null;
        ListEntity list = new ListEntity();
        list.setId(dto.getId());
        list.setName(dto.getName());
        // boardId sera résolu dans le service (lookup du Board)
        return list;
    }
}
