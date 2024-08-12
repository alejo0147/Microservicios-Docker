package com.algorian.springcloud.msvc.cursos.services;

import com.algorian.springcloud.msvc.cursos.models.Usuario;
import com.algorian.springcloud.msvc.cursos.models.entity.Curso;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;

public interface ICursoService {

    List<Curso> listar();

    Optional<Curso> porId(Long id);

    Curso guardar(Curso curso);

    void eliminar(Long id);

    void eliminarCurUsuPorId(Long id);
    
    void generateExcel(HttpServletResponse response);


    //  MÉTODOS EXTERNOS CON LÓGICA DE NEGOCIO CON DATOS QUE SE OBTIENEN DE OTRO SERVICIO

    //  RECIBE EL USUARIO Y EL ID DEL CURSO PARA AGREGAR A LA TABLA cursos_usuarios
    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);    //  ASIGNAR UN USUARIO EXISTENTE EN BBDD MYSQL

    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);      //  CREAR DESDE MSVC_CURSO UN USUARIO EN BBDD MYSQL

    Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId);   //  DESASIGNAR UN USUARIO EN UN CURSO

    Optional<Curso> porIdConUsuarios(Long id);

}
