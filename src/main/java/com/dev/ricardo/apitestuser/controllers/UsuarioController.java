package com.dev.ricardo.apitestuser.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dev.ricardo.apitestuser.dto.UsuarioDTO;
import com.dev.ricardo.apitestuser.dto.UsuarioResponseDTO;
import com.dev.ricardo.apitestuser.dto.UsuarioUpdateDTO;
import com.dev.ricardo.apitestuser.services.UsuarioService;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

	@GetMapping
	public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
        List<UsuarioResponseDTO> listDTO = service.findAll();
        return ResponseEntity.ok().body(listDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long id) {
        UsuarioResponseDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
	}

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> register(@RequestBody UsuarioDTO dto) {
        UsuarioResponseDTO dtoResp = service.register(dto);
        		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dtoResp);
    }

    @PutMapping(value = "/{idUser}")
	public ResponseEntity<UsuarioResponseDTO> update(@PathVariable Long idUser, @RequestBody UsuarioUpdateDTO dto) {
		UsuarioResponseDTO dtoResp = service.update(dto, idUser);
		return ResponseEntity.ok().body(dtoResp);
	}

    @PatchMapping(value = "/{idUser}")
    public ResponseEntity<UsuarioResponseDTO> updatePatch(@PathVariable Long idUser, @RequestBody Map<String, Object> fields) {
        UsuarioResponseDTO dtoResp = service.updatePatch(fields, idUser);
        return ResponseEntity.ok().body(dtoResp);
    }
}
