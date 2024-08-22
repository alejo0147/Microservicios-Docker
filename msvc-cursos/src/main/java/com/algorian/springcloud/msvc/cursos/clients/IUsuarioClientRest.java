package com.algorian.springcloud.msvc.cursos.clients;

import com.algorian.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="msvc-usuarios", url="${msvc.usuarios.url}")
public interface IUsuarioClientRest {

    @GetMapping("/{id}")
    Usuario detalle(@PathVariable(name="id") Long id);

    @PostMapping("/")
    Usuario crear (@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> obtenerUsuPorId(@RequestParam  Iterable<Long> ids);

}
