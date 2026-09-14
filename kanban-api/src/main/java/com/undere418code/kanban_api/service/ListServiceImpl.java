package com.undere418code.kanban_api.service;

import java.lang.reflect.Field;
import java.util.List;

import org.openapitools.jackson.nullable.JsonNullable;
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
import com.undere418code.kanban_api.util.FieldCheckUtils;
import com.undere418code.kanban_api.util.JsonNullablePatch;

@Service
public class ListServiceImpl implements ListService{

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;
    private final ListMapper listMapper;

    public ListServiceImpl(ListRepository listRepository, BoardRepository boardRepository, ListMapper listMapper) {
        this.listRepository = listRepository;
        this.boardRepository = boardRepository;
        this.listMapper = listMapper;
    }

    private int nextPosition(Long boardId) {
        return listRepository.countByBoardId(boardId);
    }

    @Override 
    public ListDTO createList(CreateListDTO dto) {

        BoardEntity board = boardRepository.findById(dto.boardId())
                .orElseThrow(() -> new NotFoundException("Board " + dto.boardId() + " not found for creating"));

        ListEntity list = listMapper.toEntity(dto);
        list.setBoard(board);
        list.setPosition(nextPosition(dto.boardId()));

        ListEntity saved = listRepository.save(list);
        return listMapper.toDTO(saved);
    }

    @Override 
    public ListDTO getList(Long id) {
        ListEntity list = listRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("List " + id + " not found for getting"));
        return listMapper.toDTO(list);
    }

    @Override 
    public List<ListDTO> getListsByBoard(Long boardId) {
        return listRepository.findByBoardId(boardId)
                .stream()
                .map(listMapper::toDTO)
                .toList();
    }

    @Override 
    public ListDTO updateList(Long id, UpdateListDTO dto) {
        ListEntity list = listRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("List " + id + " not found for update"));
        
        
        if(JsonNullablePatch.applyPatch(dto.name(), list::setName)){
            FieldCheckUtils.requireNotBlankaAndNotNull(list.getName(), "List Name");
        }

        // Repositionnement
        if(dto.position().isPresent()){
            int oldPos = list.getPosition();
            int newPos = dto.position().get();
            FieldCheckUtils.requireNotNullandPositive(dto.position().get(), "List Position");

            if (oldPos != newPos) {
                Long boardId = list.getBoard().getId();
                List<ListEntity> lists = listRepository.findByBoardId(boardId);

                lists.removeIf(c -> c.getId().equals(list.getId()));
                int index = Math.max(0, Math.min(newPos, lists.size()));
                lists.add(index, list);

                // Passe 1 : positions temporaires hors plage (négatives), aucune collision
                // possible
                int tempPos = -1;
                for (ListEntity c : lists) {
                    c.setPosition(tempPos--);
                }
                listRepository.saveAll(lists);
                listRepository.flush(); // force le flush avant la 2e passe

                // Passe 2 : positions finales
                int pos = 0;
                for (ListEntity c : lists) {
                    c.setPosition(pos++);
                }
                listRepository.saveAll(lists);
            }
        }

        return listMapper.toDTO(listRepository.save(list));
    }

    @Override 
    public void deleteList(Long id) {
        ListEntity list = listRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("List " + id + " not found for delete"));

        Long boardId = list.getBoard().getId();
        int deletedPos = list.getPosition();

        // Supression de la list
        listRepository.delete(list);

        // Récupère les listes restantes
        List<ListEntity> lists = listRepository.findByBoardId(boardId);

        // Recalcule les positions
        lists.stream()
                .filter(c -> c.getPosition() > deletedPos)
                .forEach(c -> c.setPosition(c.getPosition() - 1));

        listRepository.saveAll(lists);
    }
}
