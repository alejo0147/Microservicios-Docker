package com.algorian.springcloud.msvc.cursos.entity;

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

        //  VALIDAR SI EL OBJETO SÍ ES UNA INSTANCIA DE CursoUsuario
        if (this == obj) return  true;
        //  VALIDAR SI EL OBJETO NO ES UNA INSTANCIA DE CursoUsuario
        if (!(obj instanceof CursoUsuario cursoUsuario)) return false;

        //  SI ES DIFERENTE A NULO Y EL ID ES IGUAL, NOS DEVOLVERÁ EL ID
        return this.usuarioId != null && this.usuarioId.equals(cursoUsuario.usuarioId);
    }
}