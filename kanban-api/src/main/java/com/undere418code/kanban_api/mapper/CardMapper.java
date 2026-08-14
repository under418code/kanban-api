package com.undere418code.kanban_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.undere418code.kanban_api.dto.CardDTO;
import com.undere418code.kanban_api.entity.CardEntity;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "list.id", target = "listId")
    CardDTO toDTO(CardEntity entity);
}
