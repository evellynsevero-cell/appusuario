package com.example.appusuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioRequest {
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    private String email;

    public @NotBlank(message = "O nome é obrigatório") @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres") String getNome() {
        return nome;
    }

    public void setNome(@NotBlank(message = "O nome é obrigatório") @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres") String nome) {
        this.nome = nome;
    }

    public @NotBlank(message = "O e-mail é obrigatório") @Email(message = "Informe um e-mail válido") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "O e-mail é obrigatório") @Email(message = "Informe um e-mail válido") String email) {
        this.email = email;
    }

    public UsuarioRequest(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }
}

