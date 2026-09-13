package com.undere418code.kanban_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.undere418code.kanban_api.dto.CreateListDTO;
import com.undere418code.kanban_api.dto.ListDTO;
import com.undere418code.kanban_api.entity.ListEntity;

import org.mapstruct.ReportingPolicy;
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ListMapper {

    @Mapping(source = "board.id", target = "boardId")
    ListDTO toDTO(ListEntity entity);

    ListEntity toEntity(CreateListDTO dto);
}
