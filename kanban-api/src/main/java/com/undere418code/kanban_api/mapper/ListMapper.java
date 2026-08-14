package com.undere418code.kanban_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.undere418code.kanban_api.dto.ListDTO;
import com.undere418code.kanban_api.entity.ListEntity;

@Mapper(componentModel = "spring")
public interface ListMapper {

    @Mapping(source = "board.id", target = "boardId")
    ListDTO toDTO(ListEntity entity);
}
