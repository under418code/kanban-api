package com.undere418code.kanban_api.service;

import java.util.List;

import com.undere418code.kanban_api.dto.CreateListDTO;
import com.undere418code.kanban_api.dto.ListDTO;
import com.undere418code.kanban_api.dto.UpdateListDTO;

public interface ListService {
    ListDTO createList(CreateListDTO dto);
    ListDTO getList(Long id);
    List<ListDTO> getListsByBoard(Long id);
    ListDTO updateList(Long id, UpdateListDTO dto);
    void deleteList(Long id);
}
