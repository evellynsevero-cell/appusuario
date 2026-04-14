package com.example.appusuario;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UsuarioRepositoryTest {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setup() {
        var usuario = new Usuario();
        usuario.setNome("Ana Silva");
        usuario.setEmail("ana@email.com");
        usuarioRepository.save(usuario);

    }

    @Test
    void deveRetornarTrueQuandoEmailExiste() {
        assertThat(usuarioRepository.existsByEmail("ana@email.com")).isTrue();
    }

    @Test
    void deveRetornarFalseQuandoEmailNaoExiste() {
        assertThat(usuarioRepository.existsByEmail("outro@email.com")).isFalse();

    }

    @Test
    void deveEncontrarUsuarioPorEmail() {
        // Arrange
        Usuario usuario = new Usuario(null, "Evellyn", "eve@email.com");
        usuarioRepository.save(usuario);

        // Act
        Optional<Usuario> resultado = usuarioRepository.findByEmail("eve@email.com");

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Evellyn");
    }
    @Test
    void deveVerificarExistenciaPorEmail() {
        // Arrange
        usuarioRepository.save(new Usuario(null, "Eve", "eve@email.com"));

        // Act & Assert
        assertThat(usuarioRepository.existsByEmail("eve@email.com")).isTrue();
        assertThat(usuarioRepository.existsByEmail("naoexiste@email.com")).isFalse();

    }
}


