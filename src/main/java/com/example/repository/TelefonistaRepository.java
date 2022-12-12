package com.example.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Telefonista;

/*Clase repositorio que hace consultas a la BBDD con respecto a Telefonista*/

@Repository
public interface TelefonistaRepository extends MongoRepository<Telefonista, String>{

	Optional<Telefonista> findByEmail(String email);
}
