package com.example.appusuario.user;

import com.example.appusuario.exception.ResourceNotFoundException;
import com.example.appusuario.user.model.Usuario;
import com.example.appusuario.user.model.UsuarioRequest;
import com.example.appusuario.user.model.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse criar(UsuarioRequest request) {
        log.info("Criando usuário com e-mail: {}", request.getEmail());
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            log.warn("Tentativa de cadastro com e-mail duplicado: {}", request.getEmail());
            throw new IllegalArgumentException("E-mail já cadastrado: " + request.getEmail());
        }
        // Cria a entidade Usuario a partir do request
        Usuario salvo = usuarioRepository.save(new Usuario(request.getNome(), request.getEmail()));
        log.info("Usuário criado com sucesso. ID: {}", salvo.getId());
        return new UsuarioResponse(salvo.getId(), salvo.getNome(), salvo.getEmail());
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNome(), u.getEmail()))
                .toList();
    }

    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado: " + id);
        }
        usuarioRepository.deleteById(id);
        log.info("Usuário deletado com sucesso. ID: {}", id);
    }
}