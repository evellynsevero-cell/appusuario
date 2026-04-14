package com.example.appusuario;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /v1/usuarios → 201 Created com dados válidos")
    void deveCriarUsuarioERetornar201() throws Exception {
        var request = new UsuarioRequest("Ana Silva", "ana@email.com");
        var response = new UsuarioResponse(1L, "Ana Silva", "ana@email.com");
        when(usuarioService.criar(any())).thenReturn(response);

        mockMvc.perform(post("/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Ana Silva"))
                .andExpect(jsonPath("$.email").value("ana@email.com"));
    }

    @Test
    @DisplayName("POST /v1/usuarios → 400 Bad Request com nome vazio")
    void deveRetornar400ComNomeVazio() throws Exception {
        String body = """
            { "nome": "", "email": "ana@email.com" }
        """;
        mockMvc.perform(post("/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nome").exists());
    }

    @Test
    @DisplayName("GET /v1/usuarios/99 → 404 Not Found")
    void deveRetornar404QuandoNaoEncontrado() throws Exception {
        when(usuarioService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Usuário não encontrado: 99"));

        mockMvc.perform(get("/v1/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Usuário não encontrado: 99"));
    }
    @Test
    void deveRetornar201AoCriarUsuarioValido() throws Exception {
        // Arrange
        UsuarioRequest request = new UsuarioRequest("Evellyn", "eve@email.com");
        UsuarioResponse response = new UsuarioResponse(1L, "Evellyn", "eve@email.com");
        when(usuarioService.criar(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Evellyn"));
    }

    @Test
    void deveRetornar400QuandoEmailInvalido() throws Exception {
        // Arrange
        UsuarioRequest request = new UsuarioRequest("Eve", "email-invalido");

        // Act & Assert
        mockMvc.perform(post("/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404QuandoUsuarioNaoEncontrado() throws Exception {
        // Arrange
        when(usuarioService.buscarPorId(99L)).thenThrow(new ResourceNotFoundException("Usuário não encontrado"));

        // Act & Assert
        mockMvc.perform(get("/v1/usuarios/99"))
                .andExpect(status().isNotFound());
    }
}
