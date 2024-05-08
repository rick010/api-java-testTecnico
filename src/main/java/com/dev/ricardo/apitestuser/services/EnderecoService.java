package com.dev.ricardo.apitestuser.services;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import com.dev.ricardo.apitestuser.dto.EnderecoDTO;
import com.dev.ricardo.apitestuser.entities.Endereco;
import com.dev.ricardo.apitestuser.entities.Usuario;
import com.dev.ricardo.apitestuser.repositories.EnderecoRepository;
import com.dev.ricardo.apitestuser.repositories.UsuarioRepository;
import com.dev.ricardo.apitestuser.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EnderecoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Transactional(readOnly = true)
    public List<EnderecoDTO> findAll(Long usuarioId) {
        try {
            Usuario usuario = usuarioRepository.findById(usuarioId).get();
            Set<Endereco> list = usuario.getEnderecos();
        return list.stream().map(e -> new EnderecoDTO(e)).collect(Collectors.toList());
        }catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Nenhum usuário encontrado com id: " +usuarioId);
        }catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Nenhum usuário encontrado com id: " +usuarioId);
        }
    }

    @Transactional(readOnly = true)
    public EnderecoDTO findById(Long usuarioId, Long enderecoId) {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado com id: " +usuarioId));
    
            Endereco endereco = usuario.getEnderecos().stream()
                    .filter(e -> e.getId().equals(enderecoId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Nenhum endereço encontrado com id: " +enderecoId));
    
            return new EnderecoDTO(endereco);
    }

    @Transactional
    public EnderecoDTO register(EnderecoDTO dto, Long usuarioId) {
        Endereco endereco = new Endereco(dto);
        enderecoRepository.save(endereco);

        Optional<Usuario> optional = usuarioRepository.findById(usuarioId);
        Usuario usuario = optional.orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado com id: " +usuarioId));
        usuario.getEnderecos().add(endereco);
        usuarioRepository.save(usuario);

        return new EnderecoDTO(endereco);
    }

    @Transactional
    public EnderecoDTO update(EnderecoDTO dto, Long idUsuario, Long idEndereco) {
        try {
            Endereco endereco = enderecoRepository.findById(idEndereco)
                    .orElseThrow(() -> new ResourceNotFoundException("Nenhum endereço encontrado com o id: " + idEndereco));
    
            Usuario usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado com o id: " + idUsuario));
    
            var result = usuario.getEnderecos().stream().anyMatch(e -> e.getId().equals(idEndereco));
            
            if (!result) {
                throw new IllegalIdentifierException("O Endereço não pertence ao usuário");
            }
    
            endereco.setLogradouro(dto.getLogradouro());
            endereco.setCep(dto.getCep());
            endereco.setNumero(dto.getNumero());
            endereco.setCidade(dto.getCidade());
            endereco.setEstado(dto.getEstado());
            enderecoRepository.save(endereco);
    
            return new EnderecoDTO(endereco);
        } catch (EntityNotFoundException  e) {
            throw new ResourceNotFoundException("Usuário ou Endereço não encontrado");
        } catch (IllegalIdentifierException e) {
            throw new IllegalIdentifierException("O Endereço não pertence ao usuário");
        }
    }

    @Transactional
    public EnderecoDTO updatePatch(Map<String, Object> fields, Long idUsuario, Long idEndereco) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado com o id: " + idUsuario));

        Endereco endereco = enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum endereço encontrado com o id: " + idEndereco));
   
        if (!usuario.getEnderecos().contains(endereco)) {
            throw new IllegalArgumentException("O Endereço não pertence ao usuário");
        }

        Map<String, Object> enderecoFields = new HashMap<>();

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String propertyName = entry.getKey();
            Object propertyValue = entry.getValue();

           if (isEnderecoField(propertyName)) {
                enderecoFields.put(propertyName, propertyValue);
            }
        }

        mergeEndereco(enderecoFields, endereco);

        enderecoRepository.save(endereco);

        return new EnderecoDTO(endereco);
    }


    @SuppressWarnings("null")
    private void mergeEndereco(Map<String, Object> fields, Endereco entity) {
		ObjectMapper objectMapper = new ObjectMapper();
		Endereco entityConvert = objectMapper.convertValue(fields, Endereco.class);
		fields.forEach((propertyName, propertyValue) -> {
			Field field = ReflectionUtils.findField(Endereco.class, propertyName);
			field.setAccessible(true);
			
			Object newValue = ReflectionUtils.getField(field, entityConvert);
			
			ReflectionUtils.setField(field, entity, newValue);
		});
	}

    private boolean isEnderecoField(String propertyName) {
        try {
            Endereco.class.getDeclaredField(propertyName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
