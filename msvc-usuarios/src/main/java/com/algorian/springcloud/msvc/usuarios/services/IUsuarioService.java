package com.algorian.springcloud.msvc.usuarios.services;

import com.algorian.springcloud.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> listar();

    Optional<Usuario> porId(Long id);

    Usuario guardar(Usuario usuario);

    void eliminar(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    boolean existeEmail(String email);
}
