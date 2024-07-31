package com.algorian.springcloud.msvc.usuarios.controllers;

import com.algorian.springcloud.msvc.usuarios.models.entity.Usuario;
import com.algorian.springcloud.msvc.usuarios.services.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final IUsuarioService _usuarioServices;

    public UsuarioController(IUsuarioService usuarioServices) {
        _usuarioServices = usuarioServices;
    }

    @GetMapping
    public List<Usuario> listar() {
        return _usuarioServices.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = _usuarioServices.porId(id);
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) return validarCampos(result);
        if (_usuarioServices.existeEmail(usuario.getEmail()))
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electronico"));
        return ResponseEntity.status(HttpStatus.CREATED).body(_usuarioServices.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) return validarCampos(result);
        Optional<Usuario> o = _usuarioServices.porId(id);
        if (o.isPresent()) {
            Usuario usuarioDb = o.get();
            if (!usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail()) && _usuarioServices.buscarPorEmail(usuario.getEmail()).isPresent())
                return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electronico"));
            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(_usuarioServices.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> o = _usuarioServices.porId(id);
        if (o.isPresent()) {
            _usuarioServices.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    //  MÉTODOS EXTERNOS

    private static ResponseEntity<Map<String, String>> validarCampos(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        // Obtener todos los errores de campo
        result.getFieldErrors().forEach(err -> {
            // Añadir cada error al mapa
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        // Retornar el mapa de errores en la respuesta
        return ResponseEntity.badRequest().body(errores);
    }
}
