package com.algorian.springcloud.msvc.cursos.models.entity;

import com.algorian.springcloud.msvc.cursos.models.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    //  RELACIÓN
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    //  CREA ESTE CAMPO EN LA TABLA cursos_usuarios COMO ID FK
    @JoinColumn(name = "curso_id")
    private List<CursoUsuario> cursoUsuarios;

    //  NO ESTÁ MAPEADO A LA PERSISTENCIA (TABLA CURSO)
    @Transient
    private  List<Usuario> usuarios;

    //  CONSTRUCTOR PARA LA LISTA DE cursoUsuarios
    public Curso(){
        cursoUsuarios = new ArrayList<>();
        usuarios = new ArrayList<>();
    }


    //  GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    //  MÉTODOS DE CursoUsuario
    public void addCursoUsuario(CursoUsuario cursoUsuario){
        cursoUsuarios.add(cursoUsuario);
    }

    public void removeCursoUsuario(CursoUsuario cursoUsuario){
        cursoUsuarios.remove(cursoUsuario);
    }


    //  GETTERS AND SETTERS CursoUsuario
    public List<CursoUsuario> getCursoUsuarios() {
        return cursoUsuarios;
    }

    public void setCursoUsuarios(List<CursoUsuario> cursoUsuarios) {
        this.cursoUsuarios = cursoUsuarios;
    }


    //  GETTERS AND SETTERS usuario
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
