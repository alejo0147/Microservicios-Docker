package com.algorian.springcloud.msvc.cursos.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "cursos_usuarios")
public class CursoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", unique = true)
    private Long usuarioId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return  true;
        }
        if (!(obj instanceof CursoUsuario cursoUsuario)){
            return false;
        }
        return this.usuarioId != null && this.usuarioId.equals(cursoUsuario.usuarioId);
    }
}
