package com.algorian.springcloud.msvc.cursos.services;

import com.algorian.springcloud.msvc.cursos.models.Usuario;
import com.algorian.springcloud.msvc.cursos.entity.Curso;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface ICursoService {

    ResponseEntity<List<Curso>> listar();

    //  Listar los usuarios que pertenecen a un curso
    ResponseEntity<?> porIdConUsuarios(Long id);

    Optional<Curso> porId(Long id);

    ResponseEntity<?> guardar(Curso curso, BindingResult result);

    ResponseEntity<?> editar(Curso curso, Long id, BindingResult result);

    ResponseEntity<Void> eliminar(Long id);

    ResponseEntity<Void> eliminarCurUsuPorId(Long usuarioId);

    void generateExcel(HttpServletResponse response);


    //  FeingClients a USUARIO
    //  RECIBE EL USUARIO Y EL ID DEL CURSO PARA AGREGAR A LA TABLA cursos_usuarios
    ResponseEntity<?> asignarUsuario(Usuario usuario, Long cursoId);    //  ASIGNAR UN USUARIO EXISTENTE DE BBDD MYSQL

    ResponseEntity<?> crearUsuario(Usuario usuario, Long cursoId);      //  CREAR DESDE MSVC_CURSO UN USUARIO EN BBDD MYSQL

    ResponseEntity<?> eliminarUsuario(Usuario usuario, Long cursoId);   //  DESASIGNAR UN USUARIO EN UN CURSO

}
