package com.algorian.springcloud.msvc.usuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-cursos", url="${msvc.cursos.url}")
public interface ICursoClientRest {

    @DeleteMapping("/eliminar-curso-usuario/{usuarioId}")
    ResponseEntity<Void> eliminarCursoUsuarioPorId(@PathVariable Long usuarioId);

}
