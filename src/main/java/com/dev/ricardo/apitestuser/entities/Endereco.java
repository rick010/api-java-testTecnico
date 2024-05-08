package com.dev.ricardo.apitestuser.entities;

import java.util.HashSet;
import java.util.Set;

import com.dev.ricardo.apitestuser.dto.EnderecoDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_endereco")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String logradouro;
    private String cep;
    private Integer numero;
    private String cidade;
    private String estado;

    @ManyToMany(mappedBy = "enderecos")
    private Set<Usuario> usuarios = new HashSet<>(); 

    public Endereco(Endereco entity) {
        logradouro = entity.getLogradouro();
        cep = entity.getCep();
        numero = entity.getNumero();
        cidade = entity.getCidade();
        estado = entity.getEstado();
    }

    public Endereco(EnderecoDTO entity) {
        logradouro = entity.getLogradouro();
        cep = entity.getCep();
        numero = entity.getNumero();
        cidade = entity.getCidade();
        estado = entity.getEstado();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Endereco other = (Endereco) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

}
