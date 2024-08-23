package com.algorian.springcloud.msvc.cursos.controllers;

import com.algorian.springcloud.msvc.cursos.models.Usuario;
import com.algorian.springcloud.msvc.cursos.entity.Curso;
import com.algorian.springcloud.msvc.cursos.services.ICursoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
public class CursoController {

    private final ICursoService _cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return _cursoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        return _cursoService.porIdConUsuarios(id);
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result) {
        return _cursoService.guardar(curso, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {
        return _cursoService.editar(curso, id, result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return _cursoService.eliminar(id);
    }

    //  MÉTODO ELIMINAR cursoUsuario QUE SERÁ CONSUMIDO DESDE SERVICIO usuario
    @DeleteMapping("/eliminar-curso-usuario/{usuarioId}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long usuarioId){
        return  _cursoService.eliminarCurUsuPorId(usuarioId);
    }

    @GetMapping("/excel")
    public void generateExcelreport(HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=cursos.xlsx";
        response.setHeader(headerKey, headerValue);

        _cursoService.generateExcel(response);
    }


    //  MÉTODOS COMUNICACIÓN SERVICIOS EXTERNOS
    //  MODIFICAR cursoUsuario Y ASIGNAR USUARIO Y CURSO
    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        return _cursoService.asignarUsuario(usuario, cursoId);
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        return _cursoService.crearUsuario(usuario, cursoId);
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        return _cursoService.eliminarUsuario(usuario, cursoId);
    }

}