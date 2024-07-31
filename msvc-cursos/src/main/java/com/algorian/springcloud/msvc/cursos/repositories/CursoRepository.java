package com.algorian.springcloud.msvc.cursos.repositories;

import com.algorian.springcloud.msvc.cursos.entity.Curso;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso, Long> {
}
