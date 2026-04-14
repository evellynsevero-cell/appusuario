package com.example.appusuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;

    // Injeção do repository via construtor
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

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
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
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