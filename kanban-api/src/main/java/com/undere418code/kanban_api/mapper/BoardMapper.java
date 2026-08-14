package com.undere418code.kanban_api.mapper;

import org.mapstruct.Mapper;

import com.undere418code.kanban_api.dto.BoardDTO;
import com.undere418code.kanban_api.entity.BoardEntity;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    BoardDTO toDTO(BoardEntity entity);
}
