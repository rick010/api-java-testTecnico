package com.dev.ricardo.apitestuser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.ricardo.apitestuser.entities.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long>{
  
} 
