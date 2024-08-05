package com.algorian.springcloud.msvc.cursos.repositories;

import com.algorian.springcloud.msvc.cursos.models.entity.Curso;
import org.springframework.data.repository.CrudRepository;

public interface ICursoRepository extends CrudRepository<Curso, Long> {
}
