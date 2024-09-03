package com.undere418code.kanban_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;

import com.undere418code.kanban_api.dto.CreateListDTO;
import com.undere418code.kanban_api.dto.ListDTO;
import com.undere418code.kanban_api.dto.UpdateListDTO;
import com.undere418code.kanban_api.service.ListService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(ListController.class)//recupere les endpoints complets du controller list 
class ListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    //remplace le service par un mock(fake)
    @MockitoBean
    private ListService listService;

    // ---------------------------------------------------------
    // GET /lists/{id}
    // ---------------------------------------------------------
    @Test
    void getListById_returnListDTO() throws Exception{
        // generer un retour de dto attentu d'un get par un id 
        ListDTO dto = new ListDTO(1L, 0, "First List", 2L); 
        // simulation: si listService.getList(id) appeler retourner dto
        when(listService.getList(1L)).thenReturn(dto);

        mockMvc.perform(get("/lists/1")) // Simulation d'appel http pour l'id (1) -> vas dans le service puis retourne listService.getList(1L) simuler
                .andExpect(status().isOk()) // renvoi 200 si OK (appel fais au bon endroit)
                .andExpect(jsonPath("$.id").value(1)) // regarde si id: 1
                .andExpect(jsonPath("$.position").value(0)) // regarde si position: 
                .andExpect(jsonPath("$.name").value("First List")) // regarde si name: "First List"
                .andExpect(jsonPath("$.boardId").value(2));
        
        //verifie que le controller a bien appeler le listService.getList(1L) au moins une fois
        verify(listService).getList(1L);
    }

    // ---------------------------------------------------------
    // GET /lists/board/{boardId}
    // ---------------------------------------------------------
    @Test
    void getAllListOfOneBoardId_returnListOfListDTO() throws Exception{

        List<ListDTO> lists = List.of(
                new ListDTO(19L, 0, "Organisation", 15L),
                new ListDTO(20L, 1, "Researching", 15L)
        );

        when(listService.getListsByBoard(15L)).thenReturn(lists);

        String expectedJson = objectMapper.writeValueAsString(lists);

        mockMvc.perform(get("/lists/board/15"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
        
        verify(listService).getListsByBoard(15L);
    }

    // ---------------------------------------------------------
    // POST /lists
    // ---------------------------------------------------------
    @Test
    void createList_returnCreatededListDTO() throws Exception{

        //ObjetMapper qui vas permettre la transformation de input par un mapper en JSON 
        ObjectMapper mapper = new ObjectMapper();
        //Simulation d'objet de la requette d'entree envoye pas le client 
        CreateListDTO input = new CreateListDTO("Do Chores", 1L);
        //Simulation de sortie de l'objet en sortie de requete
        ListDTO  output = new ListDTO(14L, 0, "Do Chores", 1L);

        /*  Appel de mockito(simulation de service) quand methode createList de listService est appeler
        prend n'importe quel objet DTO de la class CreateListDTO puis retourne output */
        when(listService.createList(any(CreateListDTO.class))).thenReturn(output);

        mockMvc.perform(post("/lists") // simulation de l'endpoint post a l'adresse /lists
                .contentType(MediaType.APPLICATION_JSON) // Liste de contenu attendu JSON
                .content(mapper.writeValueAsString(input))) // mapper de input en format JSON
                .andExpect(status().isOk()) // regarde si la requete passe (status 200 attendu)
                .andExpect(jsonPath("$.id").value(14)) // controlle de l'id recu 
                .andExpect(jsonPath("$.position").value(0)) // idem pour position
                .andExpect(jsonPath("$.name").value("Do Chores")) // idem pour name
                .andExpect(jsonPath("$.boardId").value(1)); // idem pour boardId

        // Verifie que listService.createList utiliser au moins une fois / relecture de l'historique mockito retoure boolean 
        verify(listService).createList(argThat(dto ->
                 dto.name().equals("Do Chores") && dto.boardId().equals(1L) 
                )); // dois renvoyer true
    }

    // ---------------------------------------------------------
    // PATCH /lists/{id} 
    // ---------------------------------------------------------
    @Test
    void updateList_returnUpdatedListDto() throws Exception{

        ObjectMapper mapper = new ObjectMapper();
        UpdateListDTO input = new UpdateListDTO("Update List", null);
        ListDTO output = new ListDTO(3L, 7, "Update List", 6L);

        when(listService.updateList(eq(3L), any(UpdateListDTO.class))).thenReturn(output);

        mockMvc.perform(patch("/lists/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.position").value(7))
                .andExpect(jsonPath("$.name").value("Update List"))
                .andExpect(jsonPath("$.boardId").value(6));
        
        verify(listService).updateList(eq(3L), argThat(dto ->
             dto.name().equals("Update List")
            ));
    }

    // ---------------------------------------------------------
    // DELETE /lists/{id}
    // ---------------------------------------------------------
    @Test
    void deleteList_returnsNoContent() throws Exception{
        doNothing().when(listService).deleteList(1L); // la methode mock retourne rien 

        mockMvc.perform(delete("/lists/1"))
                .andExpect(status().isNoContent());

        verify(listService).deleteList(1L);// Eq pas obligatoire methode simple a une variable
    }
}   
