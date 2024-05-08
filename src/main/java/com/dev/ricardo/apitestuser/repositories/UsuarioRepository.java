package com.dev.ricardo.apitestuser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.ricardo.apitestuser.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
