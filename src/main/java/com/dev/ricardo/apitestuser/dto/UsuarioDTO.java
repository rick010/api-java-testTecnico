package com.dev.ricardo.apitestuser.dto;

import java.util.Date;

import com.dev.ricardo.apitestuser.entities.Endereco;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioDTO {

    private Long id;
    private String name;
    private Date dataDeNascimento;
    private Endereco endereco;
}
