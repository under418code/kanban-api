package com.undere418code.kanban_api.mapper;

import com.undere418code.kanban_api.dto.BoardDTO;
import com.undere418code.kanban_api.entity.BoardEntity;

/**
 * Mapper manuel Board ↔ BoardDTO.
 * Permet de séparer l'entité JPA de l'API publique.
 */
public class BoardMapper {

    public static BoardDTO toDTO(BoardEntity board) {
        if (board == null) return null;
        return new BoardDTO(
                board.getId(),
                board.getName()
        );
    }

    public static BoardEntity toEntity(BoardDTO dto) {
        if (dto == null) return null;
        BoardEntity board = new BoardEntity();
        board.setId(dto.getId());
        board.setName(dto.getName());
        return board;
    }
}
