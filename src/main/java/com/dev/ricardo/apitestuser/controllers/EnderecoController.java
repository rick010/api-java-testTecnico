package com.dev.ricardo.apitestuser.controllers;

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

import com.dev.ricardo.apitestuser.dto.EnderecoDTO;
import com.dev.ricardo.apitestuser.services.EnderecoService;

@RestController
@RequestMapping(value = "/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService service;

    @GetMapping(value = "/{usuarioId}")
    public ResponseEntity<List<EnderecoDTO>> findAll(@PathVariable Long usuarioId) {
        List<EnderecoDTO> listDTO = service.findAll(usuarioId);
        return ResponseEntity.ok().body(listDTO);
    }

    @GetMapping(value = "/{usuarioId}/{enderecoId}")
    public ResponseEntity<EnderecoDTO> findById(@PathVariable Long usuarioId, @PathVariable Long enderecoId) {
        EnderecoDTO dto = service.findById(usuarioId, enderecoId);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping(value = "/{usuarioId}")
    public ResponseEntity<EnderecoDTO> register(@RequestBody EnderecoDTO dto, @PathVariable Long usuarioId) {
        EnderecoDTO enderecoDTO = service.register(dto, usuarioId);
        return ResponseEntity.ok().body(enderecoDTO);
    }

    @PutMapping(value = "/{usuarioId}/{enderecoId}")
    public ResponseEntity<EnderecoDTO> update(@RequestBody EnderecoDTO dto, @PathVariable Long usuarioId, @PathVariable Long enderecoId) {
        EnderecoDTO enderecoDTO = service.update(dto, usuarioId, enderecoId);
        return ResponseEntity.ok().body(enderecoDTO);
    }

    @PatchMapping(value = "/{usuarioId}/{enderecoId}")
    public ResponseEntity<EnderecoDTO> updatePatch(@RequestBody Map<String, Object> fields, @PathVariable Long usuarioId, @PathVariable Long enderecoId) {
        EnderecoDTO enderecoDTO = service.updatePatch(fields, usuarioId, enderecoId);
        return ResponseEntity.ok().body(enderecoDTO);
    }
}
