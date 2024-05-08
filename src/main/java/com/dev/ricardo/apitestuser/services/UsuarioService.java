package com.dev.ricardo.apitestuser.services;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import com.dev.ricardo.apitestuser.dto.UsuarioDTO;
import com.dev.ricardo.apitestuser.dto.UsuarioResponseDTO;
import com.dev.ricardo.apitestuser.dto.UsuarioUpdateDTO;
import com.dev.ricardo.apitestuser.entities.Endereco;
import com.dev.ricardo.apitestuser.entities.Usuario;
import com.dev.ricardo.apitestuser.repositories.EnderecoRepository;
import com.dev.ricardo.apitestuser.repositories.UsuarioRepository;
import com.dev.ricardo.apitestuser.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findAll() {
        List<Usuario> list = usuarioRepository.findAll(Sort.by("name"));
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum usuário encontrado");
        }
        return list.stream().map(u -> new UsuarioResponseDTO(u)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO findById(Long usuarioId) {
        Optional<Usuario> optional = usuarioRepository.findById(usuarioId);
        Usuario usuario = optional.orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado com o id: " + usuarioId));
        return new UsuarioResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO register(UsuarioDTO dto) {
        Endereco endereco = new Endereco(dto.getEndereco());
        enderecoRepository.save(endereco);

        Usuario usuario = new Usuario(dto.getName(), dto.getDataDeNascimento(), endereco.getId());
        usuario.getEnderecos().add(endereco);
        usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO update(UsuarioUpdateDTO dto, Long usuarioId) {
        try {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado com o id: " + usuarioId));
    
            Endereco endereco = usuario.getEnderecos().stream()
                    .filter(e -> e.getId() == (dto.getEnderecoPrincipal()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Nenhum endereço encontrado com id: " + dto.getEnderecoPrincipal()));
 
    
            usuario.setName(dto.getName());
            usuario.setDataDeNascimento(dto.getDataDeNascimento());
            usuario.setEnderecoPrincipal(endereco.getId());
            usuarioRepository.save(usuario);
    
            return new UsuarioResponseDTO(usuario);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
    }

    @Transactional
    public UsuarioResponseDTO updatePatch(Map<String, Object> fields, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum usuário encontrado com o id: " + usuarioId));

        Map<String, Object> usuarioFields = new HashMap<>();

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String propertyName = entry.getKey();
            Object propertyValue = entry.getValue();

           if (isUsuarioField(propertyName)) {
            usuarioFields.put(propertyName, propertyValue);
            }
        }

        mergeUsuario(usuarioFields, usuario);

        usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(usuario);
    }




    @SuppressWarnings("null")
    private void mergeUsuario(Map<String, Object> fields, Usuario entity) {
		ObjectMapper objectMapper = new ObjectMapper();
		Usuario entityConvert = objectMapper.convertValue(fields, Usuario.class);
		fields.forEach((propertyName, propertyValue) -> {
			Field field = ReflectionUtils.findField(Usuario.class, propertyName);
			field.setAccessible(true);
			
			Object newValue = ReflectionUtils.getField(field, entityConvert);
			
			ReflectionUtils.setField(field, entity, newValue);
		});
	}

    private boolean isUsuarioField(String propertyName) {
        try {
            Usuario.class.getDeclaredField(propertyName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
