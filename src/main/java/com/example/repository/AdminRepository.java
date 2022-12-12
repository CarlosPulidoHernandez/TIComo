package com.example.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Administrator;
/*Clase repositorio que hace consultas a la BBDD con respecto a Administradores*/
@Repository
public interface AdminRepository extends MongoRepository<Administrator, String>{
		
	Optional<Administrator> findById(String nif);
	Optional<Administrator> findByEmail(String email);
	
}
