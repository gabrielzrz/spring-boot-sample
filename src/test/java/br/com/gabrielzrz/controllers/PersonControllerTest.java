package br.com.gabrielzrz.controllers;

import br.com.gabrielzrz.controllers.assembler.PersonModelAssembler;
import br.com.gabrielzrz.dto.response.PersonResponseDTO;
import br.com.gabrielzrz.exception.handler.GlobalExceptionHandler;
import br.com.gabrielzrz.service.contract.DiscordWebhookService;
import br.com.gabrielzrz.service.contract.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PersonController.class, properties = "spring.profiles.active=test")
@AutoConfigureMockMvc(addFilters = false)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @MockitoBean
    private PersonModelAssembler personModelAssembler;

    private PersonResponseDTO personResponseDTO;
    private EntityModel<PersonResponseDTO> defaultEntityModel;

    @BeforeEach
    void setUp() {
        // Arrange
        personResponseDTO = new PersonResponseDTO(UUID.fromString("019a2312-f9ea-7fcf-a17e-a94654418b49"),
                "Gabriel Zorzi", "Cascavel-PR", "Masculino", LocalDate.of(1996, 7, 24));
        defaultEntityModel = EntityModel.of(personResponseDTO);
    }

    @Test
    void findById_QuandoPersonaExiste_DeveRetornar200ComPersonResponseDTO() throws Exception {
        // Arrange
        UUID id = personResponseDTO.getId();

        // when
        when(personService.findById(id)).thenReturn(personResponseDTO);
        when(personModelAssembler.toModel(personResponseDTO)).thenReturn(defaultEntityModel);

        // Act - mockMvc vai simular uma requisição HTTP
        mockMvc.perform(get("/api/person/{id}", id).accept(MediaType.APPLICATION_JSON))
        // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Gabriel Zorzi"))
                .andExpect(jsonPath("$.address").value("Cascavel-PR"))
                .andExpect(jsonPath("$.gender").value("Masculino"));

        // Verify
        verify(personService, times(1)).findById(id);
        verify(personModelAssembler, times(1)).toModel(personResponseDTO);
    }
}