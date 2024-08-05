package com.algorian.springcloud.msvc.cursos.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Long id;

    private String nombre;

    private String email;

    private String password;

}
