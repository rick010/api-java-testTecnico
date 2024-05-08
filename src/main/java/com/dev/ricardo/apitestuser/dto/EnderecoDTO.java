package com.dev.ricardo.apitestuser.dto;

import com.dev.ricardo.apitestuser.entities.Endereco;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EnderecoDTO {

    private Long id;
    private String logradouro;
    private String cep;
    private Integer numero;
    private String cidade;
    private String estado;

    public EnderecoDTO(Endereco endereco) {
        id = endereco.getId();
        logradouro = endereco.getLogradouro();
        cep = endereco.getCep();
        numero = endereco.getNumero();
        cidade = endereco.getCidade();
        estado = endereco.getEstado();
    }
}
