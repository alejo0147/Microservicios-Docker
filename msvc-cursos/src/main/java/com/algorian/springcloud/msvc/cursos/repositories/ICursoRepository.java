package com.algorian.springcloud.msvc.cursos.repositories;

import com.algorian.springcloud.msvc.cursos.entity.Curso;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ICursoRepository extends CrudRepository<Curso, Long> {

    @Modifying
    //  Se añade la siguiente anotación por lo que no es solo un consulta ↑
    @Query("DELETE FROM CursoUsuario cu WHERE cu.usuarioId=?1")
    int eliminarCursoUsuarioPorUsuarioId(Long usuarioId);

    boolean existsByNombreIgnoreCase(String nombre);
}