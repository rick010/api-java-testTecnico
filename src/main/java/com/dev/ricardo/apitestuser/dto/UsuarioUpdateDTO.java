package com.dev.ricardo.apitestuser.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioUpdateDTO {

    private Long id;
    private String name;
    private Date dataDeNascimento;
    private Long enderecoPrincipal;
}
