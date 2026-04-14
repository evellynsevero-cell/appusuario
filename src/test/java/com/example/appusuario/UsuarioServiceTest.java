package com.example.appusuario;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService")
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Nested
    @DisplayName("criar()")
    class Criar {
        @Test
        @DisplayName("deve criar usuário com sucesso quando dados são válidos")
        void deveCriarComSucesso() {
            var request = new UsuarioRequest("Evellyn", "eve@email.com");
            var usuario = new Usuario ( "Evellyn", "eve@email.com");
            usuario.setId(1L);

            when(usuarioRepository.existsByEmail("eve@email.com")).thenReturn(false);
            when(usuarioRepository.save(any())).thenReturn(usuario);

            var response = usuarioService.criar(request);

            assertEquals("Evellyn", response.getNome());
            assertEquals("eve@email.com", response.getEmail());
            verify(usuarioRepository).save(any());
        }

        @Test
        @DisplayName("deve lançar exceção quando e-mail já está cadastrado")
        void deveLancarExcecaoQuandoEmailDuplicado() {
            var request = new UsuarioRequest("Evellyn", "eve@email.com");
            when(usuarioRepository.existsByEmail("eve@email.com")).thenReturn(true);

            Exception ex = assertThrows(IllegalArgumentException.class, () -> usuarioService.criar(request));
            assertTrue(ex.getMessage().contains("E-mail já cadastrado"));
        }
    }
    @Nested
    @DisplayName("buscarPorId()")
    class BuscarPorId {
        @Test
        @DisplayName("deve retornar usuário quando ID existe")
        void deveRetornarUsuario() {
            var usuario = new Usuario("Evellyn", "eve@email.com");
            usuario.setId(1L);
            when(usuarioRepository.findById(1L)).thenReturn(java.util.Optional.of(usuario));

            var response = usuarioService.buscarPorId(1L);

            assertEquals(1L, response.getId());
            assertEquals("Evellyn", response.getNome());
            assertEquals("eve@email.com", response.getEmail());
        }
        @Test
        @DisplayName("deve lançar ResourceNotFoundException quando ID não existe")

        void deveLancarExcecaoQuandoNaoEncontrado() {
            when(usuarioRepository.findById(2L)).thenReturn(java.util.Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscarPorId(2L));
        }
    }
    @Test
    void deveDeletarUsuarioExistente() {
        Long id = 1L;
        when(usuarioRepository.existsById(id)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(id);

        usuarioService.deletar(id);

        verify(usuarioRepository).deleteById(id);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        Long id = 2L;
        when(usuarioRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.deletar(id));
    }
}
