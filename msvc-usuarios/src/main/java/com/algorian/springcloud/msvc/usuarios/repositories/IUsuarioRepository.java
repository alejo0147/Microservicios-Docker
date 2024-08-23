package com.algorian.springcloud.msvc.usuarios.repositories;

import com.algorian.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUsuarioRepository extends CrudRepository<Usuario, Long> {

    //  OPCIÓN UNO POR EMAIL
    Optional<Usuario> findByEmail(String email);

    //  OPCIÓN DOS POR EMAIL
    @Query("SELECT u FROM Usuario u WHERE u.email = ?1")
    Optional<Usuario> porEmail(String email);

    //  OPCIÓN TRES POR EMAIL
    boolean existsByEmail(String email);

}
