package com.example.appusuario.user;

import com.example.appusuario.user.model.UsuarioRequest;
import com.example.appusuario.user.model.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscar(@PathVariable Long id) {
        // Retorna 200 OK com o usuário encontrado
        // Troque para usuarioService.buscarPorId(id) se esse for o nome correto no seu service!
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        // Retorna 204 No Content
        return ResponseEntity.noContent().build();
    }
}