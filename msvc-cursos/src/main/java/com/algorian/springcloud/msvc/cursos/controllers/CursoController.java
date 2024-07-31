package com.algorian.springcloud.msvc.cursos.controllers;

import com.algorian.springcloud.msvc.cursos.entity.Curso;
import com.algorian.springcloud.msvc.cursos.services.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> o = service.porId(id);
        if (o.isPresent()) {
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result) {
        if (result.hasErrors()) return validarCampos(result);
        Curso cursoDb = service.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) return validarCampos(result);
        Optional<Curso> o = service.porId(id);
        if (o.isPresent()) {
            Curso cursoDb = o.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Curso> o = service.porId(id);
        if (o.isPresent()) {
            service.eliminar(o.get().getId());
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
