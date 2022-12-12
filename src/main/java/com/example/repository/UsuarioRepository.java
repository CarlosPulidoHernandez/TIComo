package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Usuario;
/*Clase repositorio que hace consultas a la BBDD con respecto a Usuarios*/
@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String>{

	List<Usuario> findAll();
	Optional<Usuario> findById(String nif);
	Optional<Usuario> findByEmail(String email);
}
