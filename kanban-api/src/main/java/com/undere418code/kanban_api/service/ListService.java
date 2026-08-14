package com.undere418code.kanban_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.undere418code.kanban_api.dto.ListDTO;
import com.undere418code.kanban_api.dto.CreateListDTO;
import com.undere418code.kanban_api.dto.UpdateListDTO;
import com.undere418code.kanban_api.entity.BoardEntity;
import com.undere418code.kanban_api.entity.ListEntity;
import com.undere418code.kanban_api.exception.NotFoundException;
import com.undere418code.kanban_api.mapper.ListMapper;
import com.undere418code.kanban_api.repository.BoardRepository;
import com.undere418code.kanban_api.repository.ListRepository;

@Service
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;
    private final ListMapper listMapper;

    public ListService(ListRepository listRepository, BoardRepository boardRepository, ListMapper listMapper) {
        this.listRepository = listRepository;
        this.boardRepository = boardRepository;
        this.listMapper = listMapper;
    }

    public ListDTO createList(CreateListDTO dto) {

        BoardEntity board = boardRepository.findById(dto.boardId())
                .orElseThrow(() -> new NotFoundException("Board " + dto.boardId() + " not found"));

        ListEntity list = new ListEntity();
        list.setName(dto.name());
        list.setBoard(board);

        return listMapper.toDTO(listRepository.save(list));
    }

    public ListDTO getList(Long id) {
        ListEntity list = listRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("List " + id + " not found"));
        return listMapper.toDTO(list);
    }

    public List<ListDTO> getListsByBoard(Long boardId) {
        return listRepository.findByBoardId(boardId)
                .stream()
                .map(listMapper::toDTO)
                .toList();
    }

    public ListDTO updateList(Long id, UpdateListDTO dto) {
        ListEntity list = listRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("List " + id + " not found"));

        if (dto.name() != null) list.setName(dto.name());

        return listMapper.toDTO(listRepository.save(list));
    }

    public void deleteList(Long id) {
        if (!listRepository.existsById(id)) {
            throw new NotFoundException("List " + id + " not found");
        }
        listRepository.deleteById(id);
    }
}
