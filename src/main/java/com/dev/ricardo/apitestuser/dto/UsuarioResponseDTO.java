package com.dev.ricardo.apitestuser.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dev.ricardo.apitestuser.entities.Endereco;
import com.dev.ricardo.apitestuser.entities.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioResponseDTO {
    private Long id;
    private String name;
    private Date dataDeNascimento;
    private EnderecoDTO enderecoPrincipal;

    List<EnderecoDTO> enderecos = new ArrayList<>();

    public UsuarioResponseDTO(Usuario entity, Endereco endereco) {
        id = entity.getId();
        name = entity.getName();
        dataDeNascimento = entity.getDataDeNascimento();

        enderecoPrincipal = new EnderecoDTO(endereco);
    }

    public UsuarioResponseDTO(Usuario entity) {
        id = entity.getId();
        name = entity.getName();
        dataDeNascimento = entity.getDataDeNascimento();
        Endereco endereco = entity.getEnderecos().stream()
            .filter(e -> e.getId().equals(entity.getEnderecoPrincipal()))
            .findFirst()
            .orElse(null);
        enderecoPrincipal = new EnderecoDTO(endereco);
        entity.getEnderecos().forEach(end -> this.enderecos.add(new EnderecoDTO(end)));
    }
}
