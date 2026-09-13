package com.undere418code.kanban_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.undere418code.kanban_api.dto.BoardDTO;
import com.undere418code.kanban_api.dto.CreateBoardDTO;
import com.undere418code.kanban_api.entity.BoardEntity;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {

    /** Entity → DTO */
    BoardDTO toDTO(BoardEntity entity);

    /** CreateDTO → Entity */
    BoardEntity toEntity(CreateBoardDTO dto);

}
