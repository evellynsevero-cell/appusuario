package com.example.appusuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void deveRetornar404QuandoUsuarioNaoExiste() throws Exception {
        when(usuarioService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Usuário não encontrado: 99"));

        mockMvc.perform(get("/v1/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Usuário não encontrado: 99"));
    }

    @Test
    void deveRetornar400QuandoDadosSaoInvalidos() throws Exception {
        String requestInvalido = """
            { "nome": "", "email": "nao-e-email" }
        """;

        mockMvc.perform(post("/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Dados inválidos"))
                .andExpect(jsonPath("$.campos.nome").exists())
                .andExpect(jsonPath("$.campos.email").exists());
    }
    @Test
    void deveRetornar400QuandoRegraDeNegocioViolada() throws Exception {
        String request = """
        { "nome": "Evellyn", "email": "eve@email.com" }
    """;
        when(usuarioService.criar(any())).thenThrow(new IllegalArgumentException("E-mail já cadastrado"));

        mockMvc.perform(post("/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("E-mail já cadastrado"));
    }


}